import pymongo
import urllib

from pymongo import MongoClient
#import traceback
import flask
from flask_restful import Resource, request
client = MongoClient('localhost', 27017)
db= client.hackandroll2021
database = db.profiles
matchprefs = db.prefs
chat = db.chat
#print(database.find_one())


app = flask.Flask(__name__)

@app.route('/hey') #returns the elements from the genredb just to verify that it is indeed there
def hey():
	database.update_one({'masterid':0} , {"$inc" : {"count":1}})
	return 'HelloWorld'

@app.route('/insertProfile')
def insertProfile():
	try:

		username = request.args.get('username')
		password = request.args.get('password')
		email = request.args.get('email')
		idd = database.find_one({'masterid':0} )
		
		profile = {'username' : username , 'password':password , 'email': email , 'idd' : idd['count']}
		database.insert_one(profile)
		database.update_one({'masterid':0} , {"$inc" : {"count":1}})

		return "Success"
	except Exception as e:
		print(e)
		return 'Fail'

@app.route('/getProfile')
def getProfile():
	profile={}
	
	email = request.args.get('email')
	idd = request.args.get('idd')
	#password = request.ar
	gs.get('password')
	if email==None:
		
	
	
		profile['idd'] = int(idd)
	else:
		profile['email'] = email
	print(profile)
	output = database.find_one(profile)
	print(output)
	username = output['username']
	email = output['email']
	idd = output['idd']

	return username+"," + email+"," +str(idd)

@app.route('/authenticate')
def authenticate():
	username = request.args.get('username')
	password = request.args.get('password')


	if username==None or password ==None:
		return 'false' 
	else :
		profile = {'username':username, 'password':password}
		a = database.find_one(profile)
		if a !=None:
			return a['username']+","+a['email']+"," + str(a["idd"])
		else:
			return "false"

@app.route('/updateChat')
def updateChat():
	msg = urllib.parse.unquote(request.args.get('msg'))
	idd1 = int(request.args.get('idd1'))
	idd2 = int(request.args.get('idd2'))

	chat.update_one({'idd1':idd1, 'idd2' : idd2} , {"$set":{'msg':msg} },upsert=True)
	chat.update_one({'idd1':idd2, 'idd2' : idd1} , {"$set":{'msg':msg} },upsert=True)
	return request.args.get('msg')
@app.route('/getChat')
def getChat():
#	msg = urllib.parse.unquote(request.args.get('msg'))
	idd1 = int(request.args.get('idd1'))
	idd2 = int(request.args.get('idd2'))

	msg = chat.find_one({'idd1':idd1, 'idd2' : idd2})
	
	return msg['msg']

@app.route('/contacts')
def contacts():
	idd1 = int(request.args.get('idd1'))
	chats = chat.find({'idd1':idd1})
	finallist=""
	for i in chats:
		finallist+=str(i['idd2'])+","
	finallist = finallist.strip().split(',')[0:-1]
	finalstring = ""
	for i in finallist:
		for j in database.find({'idd':int(float(i))}):
			finalstring+=j['username']+","+j["email"]+","+str(j['idd'])+"#"




	return finalstring

@app.route("/updateDietPref")
def updateDietPref():
	try:
		diet = request.args.get('pref')

		if diet==None:
			diet="nopref"
	except:
		diet="NONE"
	idd = int(request.args.get('idd'))
	if diet!='NONE' and diet!='nopref':
		database.update({'idd':idd},{"$set":{'diet':diet}})
	profiles = database.find({"idd":idd})
	print(profiles[0])
	
	return profiles[0]['username']+","+profiles[0]['email']+","+profiles[0].get('diet' , "NOPREF")

@app.route('/findmatch')
def findmatch():
	try:
		idd1 = int(request.args.get('idd'))
		cuisine = request.args.get('cuisine')
		timerange = request.args.get('timerange');
		pricelim = request.args.get('pricelim');
		matchprefs.update({'idd':idd1} , {'$set':{'cuisine':cuisine}},upsert = True)
		matchprefs.update({'idd':idd1} , {'$set':{'timerange':timerange}},upsert = True)
		matchprefs.update({'idd':idd1} , {'$set':{'pricelim':pricelim}},upsert = True)
		potentialMatches = matchprefs.find({'timerange':timerange, 'idd':{'$ne': idd1}})
		dic ={}
		minTot = 100
		minIdx = 0
		try:
			for idx, i  in enumerate(potentialMatches):
				print(i)
				print( database.find({'idd':i['idd']})[0])
				total = 0
				if database.find({'idd':i['idd']})[0].get('diet' , 'nopref') == database.find({'idd':idd1})[0].get('diet' , 'nopref'):
					total +=0
				else:
					total+=5
				total += abs(int(i['pricelim']) - int(pricelim))
				if i['cuisine'] == cuisine:
					total+=0
				else:
					total+=10
				dic[i['idd']] = total
				if total < minTot:
					minTot = total
					minIdx = i['idd']

		except Exception as e:	
			print("error is " , e)

		print('minidx' , minIdx)
		perfectMatch= matchprefs.find({'idd':minIdx})[0]
		print('perf ' , perfectMatch)



		return str(minIdx) +","+database.find({'idd':minIdx})[0].get('username' , 'namehidden') +","+perfectMatch['cuisine']+","+perfectMatch['timerange']+","+perfectMatch['pricelim']+","+database.find({'idd':minIdx})[0].get('diet' , 'nopref') 
	except Exception as e:
		print(e)
		return "false"







if __name__ == "__main__": #Running on port 3306
    app.run(host = '0.0.0.0' , port=3306)