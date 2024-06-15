from functools import wraps
import os
import jwt
from flask import jsonify, request

from .exceptions import APIException

class AuthMiddleware:

    @staticmethod
    def is_valid_token(token):
        secret = os.getenv("SECRET_KEY")
        try:
            return jwt.decode(token, secret, algorithms=["HS256"])
        except Exception as e:
            raise Exception('Token not valid')

    def authenticate(self):
        auth_token = request.headers.get('Authorization')

        if not auth_token or not self.is_valid_token(auth_token):
            raise APIException('Unauthorized', 403)
        
        data = jwt.decode(auth_token, os.getenv('SECRET_KEY'), algorithms=["HS256"])
        return data
    
def require_authentication(func):
    @wraps(func)
    def decorated_func_auth(*args, **kwargs):
        try:
            current_user = AuthMiddleware().authenticate()
        except APIException as e:
            return jsonify({'error': str(e)}), e.status_code
        return func(current_user, *args, **kwargs)

    return decorated_func_auth