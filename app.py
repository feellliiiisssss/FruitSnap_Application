import os
import datetime
from flask import Flask, request, jsonify, send_from_directory;
import jwt
from database.firestore import add_data, get_data
from flask_swagger_ui import get_swaggerui_blueprint
import json
from authorization.exceptions import APIException
from authorization.middleware import require_authentication
import config.config
from werkzeug.utils import secure_filename
import tensorflow as tf 
from PIL import Image
import numpy as np
import uuid
import io

version_api = 'v1'

app = Flask(__name__)
model = None
CLASS_NAME = ['fresh_apples','fresh_banana','fresh_orange','fresh_strawberry','rotten_apples','rotten_banana','rotten_orange','rotten_strawberry']

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
    return jsonify({'message': 'Welcome to our official API', 'statusCode': 200}), 200

@app.route('/swagger/v1.json')
def swagger_spec():
    return send_from_directory(os.path.dirname(__file__), 'swagger/v1.json')

@app.route('/register', methods=['POST'])
def register():
    data = request.get_json()
    user = get_data("users", data['username'])
    if user:
        return jsonify({"message": "User already exists"}), 400
    add_data("users", data['username'], data)
    return jsonify({"message": "User registered successfully"}), 201

@app.route('/login', methods=['POST'])
def login():
    data = request.get_json()
    user = get_data("users", data['username'])
    if not user or user['password'] != data['password']:
        return jsonify({"message": "Invalid username or password"}), 401
    token = jwt.encode({'username': data['username'], 'exp': datetime.datetime.utcnow() + datetime.timedelta(hours=24)}, os.getenv('SECRET_KEY'), algorithm="HS256")
    return jsonify({'token': token}), 200

@app.route('/verify-token', methods=['POST'])
def verify_token():
    token = request.headers.get('Authorization').split()[1]
    try:
        data = jwt.decode(token, os.getenv('SECRET_KEY'), algorithms=["HS256"])
        return jsonify({'message': 'Token is valid', 'data': data}), 200
    except jwt.ExpiredSignatureError:
        return jsonify({'message': 'Token has expired'}), 401
    except jwt.InvalidTokenError:
        return jsonify({'message': 'Invalid token'}), 401
    
@app.route('/history', methods=['GET'])
@require_authentication
def get_history(current_user):
    return get_data('history', current_user['username'])

############### PREDICT ROUTE AND CONFIGURATION #################
# UPLOAD_FOLDER = 'uploads'
# if not os.path.exists(UPLOAD_FOLDER):
#     os.makedirs(UPLOAD_FOLDER)

# app.config['UPLOAD_FOLDER'] = UPLOAD_FOLDER

def preprocess_image(image_bytes):
    image = Image.open(io.BytesIO(image_bytes)).convert('RGB')
    image = image.resize((150, 150))
    image = np.array(image) / 255.0  # Normalize to [0, 1]
    image = np.expand_dims(image, axis=0)  # Add batch dimension
    return image

@app.route('/predict', methods=['POST'])
@require_authentication
def predict_image(current_user):
    try :
        # load model from file (local) -> done
        # get image request
        if 'image' not in request.files:
            return jsonify({'status': 'fail', 'message': 'No image part in the request'}), 400
        
        file = request.files['image']
        
        if file.filename == '':
            return jsonify({'status': 'fail', 'message': 'No selected file'}), 400
        
        if file:
            image_bytes = file.read()
            image_tensor = preprocess_image(image_bytes)

        # predict image with model already loaded
        prediction = model.predict(image_tensor)
        predicted_index = np.argmax(prediction, axis=1)[0]
        class_name = CLASS_NAME[predicted_index]

        # save to firestore as history 
        add_data('history', f"{current_user['username']}", { 'predicted': class_name, 'timestamp': datetime.datetime.utcnow()})

        return jsonify({ 'predicted': class_name, 'message': 'Image has already been predicted', 'timestamp': datetime.datetime.utcnow() }), 200
    except Exception as err:
        # error
        print(err)
        raise APIException('Could not predict image', 401)


def load_model():
    url_path = 'https://storage.googleapis.com/capstone-2024-fruit/FruitSnap_model.h5'
    model_path = '/path/to/local/model.h5'
    print('Loading model from:', model_path, url_path)
    try:
        model = tf.keras.models.load_model(model_path)
        print('Model loaded successfully')
        return model
    except Exception as error:
        print('Failed to load model:', error)
        raise error

############### PREDICT ROUTE AND CONFIGURATION #################

if __name__ == 'app':
    try:
        model = load_model()
        app.run(debug=True)
    except Exception as e:
        print(f"Application failed to start: {e}")