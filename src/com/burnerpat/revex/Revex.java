/*
*	Copyright 2014 BurnerPat (https://github.com/BurnerPat)
*	
*	Licensed under the Apache License, Version 2.0 (the "License");
*	you may not use this file except in compliance with the License.
*	You may obtain a copy of the License at
*	
*		http://www.apache.org/licenses/LICENSE-2.0
*	
*	Unless required by applicable law or agreed to in writing, software
*	distributed under the License is distributed on an "AS IS" BASIS,
*	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*	See the License for the specific language governing permissions and
*	limitations under the License.
*/

package com.burnerpat.revex;

public class Revex {
	private int offset = 0;
	private String input;
	private Node root;
	
	private int undefinedMax = 10;
	
	private Revex alphabet;
	
	public Revex(String input) {
		this(input, true);		
	}
	
	private Revex(String input, boolean initAlphabet) {
		if (initAlphabet) {
			alphabet = new Revex("[A-Za-z0-9 _-]", false);
		}
		
		offset = 0;
		this.input = input;
		root = parseDecision();
	}
	
	public void setUndefinedMax(int max) {
		undefinedMax = max;
	}
	
	public int getUndefinedMax() {
		return undefinedMax;
	}
	
	public void setAlphabet(String regex) {
		int i = 0;
		for (char c = regex.charAt(i); i < regex.length(); i++) {
			if (c == '[') {
				break;
			}
		}
		if (i >= regex.length()) {
			throw new IllegalArgumentException("Regex may only contain a group");
		}
		
		for (char c = regex.charAt(i); i < regex.length(); i++) {
			if (c == ']') {
				break;
			}
		}
		if (i < regex.length()) {
			throw new IllegalArgumentException("Regex may only contain a group");
		}
		
		alphabet = new Revex(regex);
	}
	
	Revex getAlphabet() {
		return alphabet;
	}
	
	private Node decide() {
		char c = input.charAt(offset);
		
		if (c == '(') {
			offset++;
			return parseDecision();
		}
		else if (c == '[') {
			offset++;
			return parseRange();
		}
		else {
			return parseLiteral(true);
		}
	}
	
	private void parseQuantifier(Node node) {
		if (offset < input.length()) {
			char c = input.charAt(offset);
			if (c == '{') {
				offset++;
				int end = input.indexOf('}', offset);
				
				String[] temp = input.substring(offset, end).split(",");
				if (temp.length == 2) {
					node.setBounds(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]));
				}
				else {
					node.setBounds(Integer.parseInt(temp[0]), -1);
				}
				
				offset = end + 1;
			}
			else if (c == '?') {
				offset++;
				node.setBounds(0, 1);
			}
			else if (c == '+') {
				offset++;
				node.setBounds(1, -1);
			}
			else if (c == '*') {
				offset++;
				node.setBounds(0, -1);
			}
		}
	}
	
	private Node parseDecision() {
		Decision result = new Decision(this);
		Expression expr = new Expression(this);
		result.append(expr);
		
		while (offset < input.length()) {
			char c = input.charAt(offset);
			
			if (c == ')') {
				offset++;
				break;
			}
			else if (c == '|') {
				offset++;
				expr = new Expression(this);
				result.append(expr);
			}
			else {
				Node node = decide();
				if (node != null) {
					expr.append(node);
				}
			}
		}
		parseQuantifier(result);
		return result;
	}
	
	private Node parseRange() {
		if (input.charAt(offset) == '^') {
			throwError("Negotiated ranges are forbidden");
		}
		
		Decision result = new Decision(this);
		while (offset < input.length()) {
			char from = input.charAt(offset);
			if (from == ']') {
				offset++;
				break;
			}
			
			char sep = input.charAt(offset + 1);
			
			if (sep == '-' && from != '\\') {
				char to = input.charAt(offset + 2);
				if (to != ']') {
					offset += 3;
					result.append(new Range(this, from, to));
				}
				else {
					result.append(parseLiteral(false));
				}
			}
			else {
				result.append(parseLiteral(false));
			}
		}
		parseQuantifier(result);
		return result;
	}
	
	private Node parseLiteral(boolean allowQuantifier) {
		Node result = null;
		char c = input.charAt(offset);
		
		switch (c) {
			case '.': {
				result = new Wildcard(this);
				offset++;
				break;
			}
			case '\\': {
				char e = input.charAt(offset + 1);
				
				switch (e) {
					case 'w': {
						result = new PredefinedRange(this, PredefinedRange.WORD);
						break;
					}
					case 'd': {
						result = new PredefinedRange(this, PredefinedRange.DIGIT);
						break;
					}
					case 's': {
						result = new PredefinedRange(this, PredefinedRange.WHITESPACE);
						break;
					}
					case 't': {
						result = new Literal(this, '\t');
						break;
					}
					default: {
						result = new Literal(this, e);
						break;
					}
				}

				offset += 2;
				break;
			}
			case '^': {
				throwError("Usage of line start character is forbidden");
				break;
			}
			case '$': {
				throwError("Usage of line end character is forbidden");
				break;
			}
			default: {
				result = new Literal(this, c);
				offset++;
				break;
			}
		}
		
		if (allowQuantifier) {
			parseQuantifier(result);
		}
		return result;
	}
	
	public String generate() {
		return root.generate();
	}
	
	private void throwError(String error) {
		StringBuffer buffer = new StringBuffer(error);
		buffer.append('\n');
		buffer.append(input);
		buffer.append('\n');
		
		for (int i = 0; i < offset; i++) {
			buffer.append('_');
		}
		buffer.append('^');

		throw new IllegalArgumentException(buffer.toString());
	}
}
