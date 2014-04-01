Revex
=====

A "reverse regular expression" library. This is a simple java library that generates random strings based on a regular expression. Every generated string will then match the regular expression.

Usage
---

Simple initialize a `Revex` object with your regular expression and use the `generate()` methode to obtain your random strings.

	Revex revex = new Revex("(Hello|Hey) World!?");
	System.out.println(revex.generate());

If Revex encounters any errors in the regular expression, it will throw a `IllegalArgumentException` providing information about the error. Please note that this is a `RuntimeException`, so you do not need to surround the constructor with `try / catch`.

Limitations
---

- Revex is currently not able to use negotiated ranges, so regular expressions like `[^ABC]` will not be evaluated.
- Revex does currently not support all of the available shortcodes for ranges, say `\b`
- Revex does not and will never support line start and end characters `^...$`, it will rather raise an error when encountering these

License
---

Revex is licensed under the Apache License, Version 2.0.

	Copyright 2014 BurnerPat (https://github.com/BurnerPat)
	
	Licensed under the Apache License, Version 2.0 (the "License");
	you may not use this file except in compliance with the License.
	You may obtain a copy of the License at
	
		http://www.apache.org/licenses/LICENSE-2.0
	
	Unless required by applicable law or agreed to in writing, software
	distributed under the License is distributed on an "AS IS" BASIS,
	WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and
	limitations under the License.