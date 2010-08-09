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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import org.python.core.Py;
import org.python.core.PyString;
import org.python.core.PySystemState;
import org.python.util.InteractiveConsole;

import processing.core.PApplet;

public class Runner {
	private static String read(final Reader r) throws IOException {
		final BufferedReader reader = new BufferedReader(r);
		final StringBuilder sb = new StringBuilder(1024);
		String line;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		} finally {
			reader.close();
		}
	}

	public static void main(final String[] args) throws Exception {
		if (args.length < 1) {
			System.err.println("I need the path of your Python script as an argument.");
		}
		final String pathname = args[args.length - 1];
		final String[] otherArgs = new String[args.length - 1];
		System.arraycopy(args, 0, otherArgs, 0, otherArgs.length);
		final String text = read(new FileReader(pathname));

		Py.initPython();
		final InteractiveConsole interp = new InteractiveConsole();
		final String path = new File(pathname).getCanonicalFile().getParent();
		Py.getSystemState().path.insert(0, new PyString(path));
		PySystemState.packageManager.addJarDir("thirdparty/processing/lib", false);
		PySystemState.packageManager.addJarDir("thirdparty/processing/lib/pdf", false);
		final String qtJava = System.getenv("QTJAVA");
		if (qtJava != null) {
			final String dir = new File(qtJava).getParentFile().getAbsolutePath();
			PySystemState.packageManager.addJarDir(dir, false);
		}
		try {
			interp.getLocals().__setitem__(new PyString("__file__"), new PyString(pathname));
			final PAppletJythonDriver applet = new DriverImpl(interp, text);
			PApplet.runSketch(otherArgs, applet);
		} catch (Throwable t) {
			Py.printException(t);
			interp.cleanup();
			System.exit(-1);
		}
	}
}
