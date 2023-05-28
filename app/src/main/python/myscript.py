import requests
import urllib
import time
def TextToSign(path,text):
    #while(1):
        #print('HYgbygy')
        try:
            response = requests.post("https://bilalsardar-urdu-text-to-sign.hf.space/run/predict",
            json={
            "data": [
                text,
            ]}).json()

            data = response["data"]
            print('Hssy')
            url = "https://bilalsardar-urdu-text-to-sign.hf.space/file="+data[0]["name"]
            save_as = path

            data1 = urllib.request.urlopen(url)

            f = open(save_as,'wb')
            f.write(data1.read())
            f.close()

            #break
            return 'done'
        except:
            print('jghhfhf')
            return 'Wrong Input'
           # continue

