import json
import requests
import jwt
from jwt.algorithms import RSAAlgorithm

BEARER_TOKEN = "BEARER "


class AuthProvider():
    """This is the abstract class to decode JWT ID token.
        Sample Usage with Azure ID token:
            jwks_uri = "https://login.microsoftonline.com/common/discovery/v2.0/keys"
            client_id = {Your Client Id}
            auth = rbac.AuthProvider(jwks_uri, client_id)
    """

    def __init__(self, jwks_uri, client_id):
        """Args:
            - client Id: used as audience ("aud")  
            - jwks_uri: used to get public key pool
        """
        self.client_id = client_id
        self.cert_set = self.get_certs(jwks_uri)

    def get_certs(self, jwks_uri: str):
        """Get certs from jwks uri"""
        certs = requests.get(jwks_uri).json()
        return {cert['kid']: cert for cert in certs['keys']}

    def get_public_key(self, token: str):
        """ Get public key based on token kid"""
        header_data = jwt.get_unverified_header(token)
        kid = header_data['kid']
        return RSAAlgorithm.from_jwk(json.dumps(self.cert_set[kid]))

    def decode_token(self, bearer_token: str):
        """ Decode ID token with RA256 Algorithm
            Sample Usage with Azure ID token:
                decoded = auth.decode_token(token)
                username = decoded.get('preferred_username').lower()
        """
        # TODO: Process Bearer Token more elegantly
        token = bearer_token[len(BEARER_TOKEN):]
        return jwt.decode(token, self.get_public_key(token), algorithms=[
            "RS256"], audience=self.client_id)

def AzureADAuth(client_id: str):
    jwks_uri = "https://login.microsoftonline.com/common/discovery/v2.0/keys"
    auth = AuthProvider(jwks_uri, client_id)
