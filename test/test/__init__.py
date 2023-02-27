import json
from typing import Any
import requests
from requests.auth import AuthBase

__BASE = 'http://localhost:8080'
__AUTH:AuthBase = None

class HTTPBearerAuth(AuthBase):
    """Attaches HTTP Bearer Authentication to the given Request object."""

    def __init__(self, token):
        self.token = token

    def __eq__(self, other):
        return self.token == getattr(other, "token", None)

    def __ne__(self, other):
        return not self == other

    def __call__(self, r):
        r.headers["Authorization"] = f'Bearer {self.token}'
        return r

def request(method:str, endpoint:str, data:object) -> Any:
    resp = requests.request(
        method=method,
        allow_redirects=True,
        auth=__AUTH,
        url= f'{__BASE}{endpoint}',
        json=data
    )
    resp.raise_for_status()
    return resp.json

def get(endpoint, data=None) -> Any: return request('GET', endpoint=endpoint, data=data)
def put(endpoint, data=None) -> Any: return request('PUT', endpoint=endpoint, data=data)
def post(endpoint, data=None) -> Any: return request('POST', endpoint=endpoint, data=data)
def delete(endpoint, data=None) -> Any: return request('DELETE', endpoint=endpoint, data=data)

def set_auth(token):
    global __AUTH
    __AUTH = HTTPBearerAuth(token)

__all__ = ('request','get','put','post','delete','set_auth')
