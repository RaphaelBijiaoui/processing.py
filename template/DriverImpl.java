/*
 * Copyright 2010 Jonathan Feinberg
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package jycessing;

import org.python.util.InteractiveConsole;
import org.python.core.*;
import processing.core.*;
import java.io.*;

@SuppressWarnings("serial")
public class DriverImpl extends PAppletJythonDriver {

	public DriverImpl(final InteractiveConsole interp, final String programText) {
		super(interp, programText);
	}
	@Override
	protected void populateBuiltins() {
		%METHOD_BINDINGS%
	}

	@Override
	protected void setFields() {
		%FIELD_BINDINGS%
	}

}
