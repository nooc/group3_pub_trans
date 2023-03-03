import test as tst
from test.auth import authenticate
from test.route import query
from test import set_auth, set_remote

set_remote()

print('----- AUTHENTICATION -----')

jwt = authenticate('lightbringer@nixus.space')
if not jwt: exit()
set_auth(jwt)
print('JWT:', jwt)

print('----- ROUTE QUERY -----')

result = query("Studiegångens hundrastplan, Göteborg","Botaniska, Göteborg")
print(result)
