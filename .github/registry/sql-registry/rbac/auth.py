import json
import requests
import jwt
from jwt.algorithms import RSAAlgorithm


class AuthProvider():
    def __init__(self, jwks_uri, client_id):
        self.client_id = client_id
        self.cert_set = self.get_certs(jwks_uri)

    def get_certs(self, jwks_uri: str):
        certs = requests.get(jwks_uri).json()
        return {cert['kid']: cert for cert in certs['keys']}

    def get_public_key(self, token: str):
        header_data = jwt.get_unverified_header(token)
        kid = header_data['kid']
        return RSAAlgorithm.from_jwk(json.dumps(self.cert_set[kid]))

    def decode_token(self, token: str):
        return jwt.decode(token, self.get_public_key(token), algorithms=[
            "RS256"], audience=self.client_id)
