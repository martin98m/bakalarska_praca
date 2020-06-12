import base64
from Crypto.Cipher import AES
from Crypto.Util.Padding import pad, unpad


class AESModule:

    AES_key = "hesloheslohesloh".encode('utf-8')

    def __init__(self):
        self.cipher = AES.new(self.AES_key, AES.MODE_ECB)

    def encode_data(self, plaintext):
        data = plaintext.encode('utf-8')
        data = pad(data, 16)
        enc_data = self.cipher.encrypt(data)
        std_data = base64.b64encode(enc_data)
        return std_data

    def decode_data(self, plaintext):
        encoded_data = base64.b64decode(plaintext)
        dec_data = self.cipher.decrypt(encoded_data)
        dec_data = unpad(dec_data, 16)
        str_res = dec_data.decode('utf-8')
        return str_res

