+ String {
	
	getUnquotedTextIndices {|quoteChar = "\""|
		var quoteIndices;
		
		quoteIndices = this.findAll(quoteChar.asString);
		quoteIndices = quoteIndices.select{|idx, i|
			this[idx-1] != $\\
		};
		^((([-1] ++ quoteIndices ++ [this.size]).clump(2)) +.t [1, -1])
	}

	getStructuredTextIndices {
		var unquotedTextIndices;
		
		unquotedTextIndices = this.getUnquotedTextIndices;
		unquotedTextIndices = unquotedTextIndices.collect{|idxs| 
			this.copyRange(*idxs).getUnquotedTextIndices($') + idxs.first
		}.flat.clump(2);
		
		^unquotedTextIndices
	}

	prepareForJSonDict {
		var newString = this.deepCopy;
		var idxs;
		idxs = newString.getStructuredTextIndices;
	
	
		idxs.do{|pairs, i| 
			Interval(*pairs).do{|idx|
				(newString[idx] == ${).if({newString[idx] = $(});
				(newString[idx] == $}).if({newString[idx] = $)});
			
				(newString[idx] == $:).if({
					[(idxs[i-1].last)+1, pairs.first-1].do{|quoteIdx|
						newString[quoteIdx] = $'
					}
				});
			}
		};
		^newString
	}

	jsonToDict {
		^(this.prepareForJSonDict.interpret)
	}

}