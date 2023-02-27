import test as tst
from cryptography.hazmat.primitives import hashes, serialization
from cryptography.hazmat.backends import default_backend
from cryptography.hazmat.primitives.asymmetric import padding
from base64 import urlsafe_b64encode as b64encode


with open('..\\default\\src\\main\\resources\\pub.der','rb') as f:
    pubKey = serialization.load_der_public_key(f.read(), backend=default_backend())

print('-----\nAUTHENTICATION\n-----')
try:
    resp = tst.post('/auth/request', {'user':'lightbringer@nixus.space'})
    if resp:
        value = resp()['value']
        print(value)

        clallenge_response = dict(
            value= value,
            encrypted= b64encode(pubKey.encrypt(value.encode())).decode()
        )
        print('SENDING')
        print(clallenge_response)
        resp = tst.post('/auth/validate',clallenge_response)
        if resp:
            token = resp()['token']
            print(token)

        print('-----\n')

except Exception as ex:
    print(ex)

