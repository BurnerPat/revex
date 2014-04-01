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

package com.burnerpat.revex.test;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.burnerpat.revex.Revex;

public class RevexTest {
	private static final List<String> INPUT = Arrays.asList(
			"[A-Za-z]{1,10} [A-Za-z0-9 ]{1,10}",
			"TEST",
			"(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)",
			"Lorem ipsum dolor sit amet [^A-Z].*",
			"[a-z0-9_-]{3,16}",
			"[a-z0-9_-]{6,18}",
			"\\d\\s\\w\\^"
	);
	
	public static void main(String[] args) {
		for (String str : INPUT) {
			try {
				Revex revex = new Revex(str);
				Pattern pattern = Pattern.compile(str);
				
				System.out.println(str);
				
				for (int i = 0; i < 10; i++) {
					String s = revex.generate();
					Matcher m = pattern.matcher(s);
					System.out.println(m.find() + ":\t" + s);
				}
				
				System.out.println();
			}
			catch (Exception ex) {
				System.err.println();
				ex.printStackTrace(System.err);
			}
		}
	}
}
