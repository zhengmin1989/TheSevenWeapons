from M2Crypto.EVP import Cipher
from base64 import b64encode, b64decode

key = b64decode('H5jOqyCXcO+odcJFhT7Odh+Yzqsgl3Dv')
iv = b64decode('AAoKCgoCAqo=')
ciphertext = '5458d715704493d8e6b9bd38f8b6be0e'.decode('hex')
decipher = Cipher(alg='des_ede3_cbc', key=key, op=0, iv=iv)
plaintext = decipher.update(ciphertext)
plaintext += decipher.final()
print plaintext


