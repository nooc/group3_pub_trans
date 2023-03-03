import rsa
from base64 import b64encode as b64encode
from . import *

def authenticate(user:str) -> str:
    """Authenticate

    Returns:
        str: JWT
    """
    print('Reading rsa public key.')
    with open('..\\default\\src\\main\\resources\\pub.der','rb') as f:
        pubKey = rsa.PublicKey.load_pkcs1_openssl_der(f.read())

    print('Requesting authentication challenge.')
    resp = post('/auth/request', {'user':user})
    if resp:
        value = resp()['value']
        clallenge_response = dict(
            value= value,
            encrypted= b64encode(rsa.encrypt(value.encode(), pubKey)).decode()
        )
        print('Responding to authentication challenge.')
        resp = post('/auth/validate',clallenge_response)
        if resp:
            print('Extracting token.')
            return resp()['jwt']

