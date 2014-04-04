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

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class Node {
	private List<Node> children;
	
	private int min = 1;
	private int max = 1;
	private final Revex parent;
	
	public Node(Revex parent) {
		this.parent = parent;
		children = new ArrayList<Node>();
	}
	
	protected Revex getParent() {
		return parent;
	}
	
	public void append(Node child) {
		children.add(child);
	}
	
	public Node[] getChildren() {
		return children.toArray(new Node[children.size()]);
	}
	
	public void setBounds(int min, int max) {
		this.min = min;
		this.max = max;
	}
	
	public String generate() {
		StringBuffer buffer = new StringBuffer();
		int amt = min;
		if (min != max) {
			Random rand = new Random();
			if (max >= 0) {
				amt += rand.nextInt(max - min + 1);
			}
			else {
				amt += rand.nextInt(parent.getUndefinedMax());
			}
		}
		for (int i = 1; i <= amt; i++) {
			buffer.append(doGenerate());
		}
		return buffer.toString();
	}
	
	protected abstract String doGenerate();
}
