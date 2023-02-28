# Microservices school project

Microservices school project for the course *Backend programming in Java and Spring Boot*.

## About

This repository contains microservices.
The default (and currently only) service is running on Google App Engine Standard with Java17 runtime.

**Default** is written using String Boot and use Googles Datastore as data backend.

Authentication is achieven using an encryptioon challenge and authorization uses JWT bearer tokens.

## Deployment info

**Default** is deployed to Google App Engine using gcloud like so:

```console
cd default
gcloud app deploy --project &lt;google project id&gt;
```

## Authentication flow

### Step 1

Make an authentication request:

	POST /auth/request

with json containing your user name:

	{
		"user": "<your user name>"
	}

You will get a json response like this example:

	{
		"value": "f25aada6-b270-4f4e-8d50-64049e1f6b25"
	}

### Step 2

Use the provided rsa key to encrypt this value and encode it as a base-64 string. Then make a verification query:

	POST /auth/verify

with json containing your response:

	{
		"value": "f2aad5a6-b270-4f4e-8d50-64f6b2049e15",
    "encrypted": "T87h38sZn5rqFdg0VMxW4OR5xb02tyYs/bqoEqUhsbo3v+qlq5Ewc1vShdGHQvbOmRw3X6d56dBg8Xehzt+34izCGKKwGeFLg1tezEoeKpv1MTdosJcTCRGKhf6nxFiG80oEJC7M+OWIpC9EBR15bm/U7T6Hk47weaKfgwfyYu5amdZfDsgSYrxF9opQ1McSICsjlAvPGU2l60NlX85yPEVmxIwGi4PXRANJJ8p2dzHkPRrjvYOnJ284u7iVZwU4vGV9Lqyjil3BhmdOtoV0ew3JtqrUoQiiWOwDRkHOfargYYX49yU5oOOmzEFRpgNMnerChnTJsNKQwQPqxn4Iwg=="
	}

You will get a json response containing a jwt token:

	{
		"jwt": "eyJ0eXLCJhbGciOiJSAiOiJKV1QiUzI1NiJ9.eyJpc3MiOiJzcGFjZS5uaXh1cy5taWNyby1zZXJ2aWNlcy0zNzg0MTUiLCJleHAiOjE2Nzc1OTgzMTksInVzZXJuYW1lIjoibGlnaHRicmluZ2VyQG5peHVzLnNwYWNlIn0.VFPY9Xzlh6jOhWMMMpdpvtmtVkGqhU10J_1SBHrl0-6ynATPnQf_KlM3sTJDw_qwitZKvAZ6OTcea9sUz7_2oYF8JCnwL2NLSylfGlKrVtP3rpa4NNcMe4wHTKL-iAse9ql6eqP9kxU9YVK2HNmf7lK6v2WAFYBzYb_RYpOG2wTyUh63egJWEMk2I4-8zBu5Ro0i2aZktlT18OnYvU9sEFM9t8D1FeOXLTve1ZtoL-AA-6Gzp16s1bBRiEjyr6yX7VtkyvTlvfyj_vWQKyWs0-lyqHB7glepKWh-Dggh0sY-CJEwLFkZUhanprAlAuhVTgypujGjIr_5HLvkjAYGkA"
	}

### Step 3

Use the jwt token as the bearer value in the authorization header when calling all other endpoints:

	Authorization: Bearer eyJ0eXLCJhbGciOiJSA...

### Encryption example

```
import java.util.Base64;
import javax.crypto.Cipher;
import java.security.KeyFactory;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.KeySpec;
import java.security.spec.X509EncodedKeySpec;

class Example {

	private RSAPublicKey publicKey;
  private Cipher cipher;
  
	public Example() {
	// Get cipher for encryptiong.
	this.cipher = Cipher.getInstance("RSA");
  
	// Get key in resources folder.
	var file = ResourceUtils.getFile("classpath:pub.der");
  
	// Read key
	try (var strm = new FileInputStream(file)) {
  
    var bytes = strm.readAllBytes();
    
    // Create the RSAPublicKey.
    KeySpec spec = new X509EncodedKeySpec(bytes, "RSA");
    publicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
    	.generatePublic(getKeySpec("pub.der", false));
    }
  }

  // encrypt value, for example "f25aada6-b270-4f4e-8d50-64049e1f6b25".
	public static String encrypt(String value) {
    // Init encryption.
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    
    // Encrypt in one go.
    byte[] encryptedBytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));
    
    // Encrypted and base64 converter string.
    return Base64.getEncoder().encodeToString(encryptedBytes);
  }
}

```