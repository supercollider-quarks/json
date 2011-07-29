Gist {
	var <dict;
	var <id;
	
	*newFrom{|dict|
		var gist;
		
		gist = this.new(dict.id);
		gist.updateDictWith(dict);
		
		^gist
	}
	
	*new{|id|
		^super.new.init(id) 	
	}
	
	
	init {|argID|
		dict = ();
		id = argID;	
	}
	
	doesNotUnderstand {|selector ... args|
		^dict.perform(selector, *args)
	}
	
	
	updateDictWith {|aDict|
		dict = dict.composeEvents(aDict);
	}
	
	*allGistsFor {|user, username, password|
		var options, gistDicts;
		
		options = password.notNil.if({
			"-u %:%".format(username, password)
		}, {
			""	
		});
		
		gistDicts = ("https://api.github.com/users/%/gists".format(user).curl(options: options)).jsonToDict;
		
		^gistDicts.collect{|dict|
			this.newFrom(dict)
		}
	}
	
	grabFromGistRepo {
		this.updateDictWith("https://api.github.com/gists/%".format(id).curl.jsonToDict)
	}
	
	filenames {
		^this.files.keys
	}
	
	at{|filename|
		^this.files[filename].content
	}
	
	content {
		^this.files.collect(_.content)	
	}
	
	*contentAsJsonString {|contentDict|
		var result = "{\n\n";
		var  numCommas = contentDict.size - 1;
		
		contentDict.keysValuesDo({|key, val, i|
			result = result ++ "%: {\"content\":\n %}%\n".format(key.asString.quote, 
				val
				.replace(
					"\\", 
					"\\\\"
				)
				.replace(
					"'", 
					"'\\''"
				)
				.escapeChar($").quote, (i < numCommas).if({","}, {""}))
		});
		
		result = result++ "\n}";
		^result
	}
	
	
	*postToRepo {|descr, content, public = true, username, password|
		var options, jsonString;
		
		jsonString = "\'{\"description\": \"%\",\n\"public\": %,\n\"files\": %}\'"
			.format(descr, public, this.contentAsJsonString(content));
	
		
		options = username.notNil.if({
			"-u %:%".format(username, password)
		}, {
			""	
		});
	
		options = options + "-d %".format(jsonString);
		
		^this.newFrom("https://api.github.com/gists".curl(options: options).jsonToDict);
	}
}