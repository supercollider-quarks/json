Gist {
	*allGistsFor {|user, username, password|
		var options;
		
		options = password.notNil.if({
			"-u %:%".format(username, password)
		}, {
			""	
		});
	
		^("https://api.github.com/users/%/gists".format(user).curl(options: options)).jsonToDict
	}
	
	
	*getGist {|id, username, password|
		var options;
		options = username.notNil.if({
			"-u %:%".format(username, password)
		}, {
			""	
		});
		
		^("https://api.github.com/gists/%".format(id).curl(options: options)).jsonToDict
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
	
	
	*postGist {|descr, content, public = true, username, password|
		var options, jsonString;
		
		jsonString = "\'{\"description\": \"%\",\n\"public\": %,\n\"files\": %}\'"
			.format(descr, public, this.contentAsJsonString(content));
	
		
		options = username.notNil.if({
			"-u %:%".format(username, password)
		}, {
			""	
		});
	
		options = options + "-d %".format(jsonString);
		
		^"https://api.github.com/gists".curl(options: options)
	}
}