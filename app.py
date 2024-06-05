import os
import datetime
from flask import Flask, request, jsonify, send_from_directory;
import jwt
from database.firestore import add_data, get_data
from flask_swagger_ui import get_swaggerui_blueprint
import json
from authorization.exceptions import APIException
from authorization.middleware import require_authentication

app = Flask(__name__)
app.config.from_file("./config/config.json", load=json.load)

SWAGGER_URL = '/api/docs'  
API_URL = "/swagger/v1.json"   

swaggerui_blueprint = get_swaggerui_blueprint(
    SWAGGER_URL, 
    API_URL,
    config={  
        'app_name': "Test application"
    },
)

app.register_blueprint(swaggerui_blueprint)

@app.errorhandler(APIException)
def handle_exception(error):
    response = jsonify(error.to_dict())
    response.status_code = error.status_code
    return response

@app.route("/")
def index_page():
    return jsonify({'message': 'Welcome to our official API', 'statusCode': 200 }), 200

@app.route('/swagger/v1.json')
def swagger_spec():
    return send_from_directory(os.path.dirname(__file__), 'swagger/v1.json')

@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    user = get_data("users", data['username'])
    if user.exists:
        return jsonify({"message": "User already exists"}), 400
    add_data("users", data['username'], data)
    return jsonify({"message": "User registered successfully"}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    user = get_data("users", data['username'])
    if not user.exists or user.to_dict()['password'] != data['password']:
        return jsonify({"message": "Invalid username or password"}), 401
    token = jwt.encode({'username': data['username'], 'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=24)}, os.getenv('SECRET_KEY'), algorithm="HS256")
    return jsonify({'token': token}), 200

@app.route('/verify-token', methods=['POST'])
def verify_token():
    token = request.headers.get('Authorization').split()[1]
    try:
        data = jwt.decode(token, app.config['SECRET_KEY'], algorithms=["HS256"])
        return jsonify({'message': 'Token is valid', 'data': data}), 200
    except jwt.ExpiredSignatureError:
        return jsonify({'message': 'Token has expired'}), 401
    except jwt.InvalidTokenError:
        return jsonify({'message': 'Invalid token'}), 401
    
@app.route('/predict', methdos=['POST'])
@require_authentication
def predict_image():
    # get image request

    # load model from file (local)

    # predict image with model already loaded

    # error
    # raise APIException('Could not predict image', 401)

    return jsonify({ 'message': 'Image has already been predicted', 'statusCode': 200 }), 200

if __name__ == "__main__":
    app.run(debug=True)