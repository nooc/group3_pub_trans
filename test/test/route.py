from . import *

def query(src:str, dst:str) -> dict:

    print('Querying route.')
    print(src, '-->', dst)
    resp = post('/routes/query', dict(
        source=src,
        destination=dst
    ))
    return resp
