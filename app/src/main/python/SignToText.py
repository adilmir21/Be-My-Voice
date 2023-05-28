import base64
import requests

def SignToText(path):
#     with open(path, "rb") as f1:
#         encoded_f1 = base64.b64encode(f1.read())
#         encoded_f1=str(encoded_f1,'ascii', 'ignore')
    #filedata="data:video/mp4;base64,"+path
    jeesoon={"name":"test.mp4","data":path}
    response = requests.post("https://bilalsardar-urdu-sign-to-text.hf.space/run/predict",
    json={
      "data": [
        jeesoon
      ]
    }).json()
    data = response["data"]
    #return data[0]
    return data
