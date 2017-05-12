/**
 * Copyright (C) 2012 cogroo <cogroo@cogroo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cogroo;


import java.io.IOException;
import java.util.Locale;
import java.util.Scanner;

import org.cogroo.analyzer.Analyzer;
import org.cogroo.analyzer.ComponentFactory;
import org.cogroo.checker.CheckDocument;
import org.cogroo.checker.GrammarChecker;
import org.cogroo.text.Document;
import org.cogroo.text.impl.DocumentImpl;
import org.cogroo.util.TextUtils;


/**
 * 
 */
public class CLI {
  /**
   * @param args
   *          the language to be used, "pt_BR" by default
 * @throws IOException 
 * @throws IllegalArgumentException 
   */
  public static void main(String[] args) throws IllegalArgumentException, IOException {

    long start = System.nanoTime();

//    if (args.length != 1) {
//      System.err.println("Language is missing! usage: CLI pt_br");
//      return;
//    }

    ComponentFactory factory = ComponentFactory.create(new Locale("pt", "BR"));

    Analyzer pipe = factory.createPipe();
    GrammarChecker gc = new GrammarChecker(pipe);

    System.out.println("Loading time ["
        + ((System.nanoTime() - start) / 1000000) + "ms]");
    Scanner kb = new Scanner(System.in);
    System.out.print("Enter the sentence: ");
    String input = kb.nextLine();

    while (!input.equals("q")) {
      if (input.equals("0")) {
        input = "";
      }

      Document document = new DocumentImpl();
      document.setText(input);
      pipe.analyze(document);
      
      
      CheckDocument document2 = new CheckDocument(input);
      
      gc.analyze(document2);

      
      
      System.out.println(TextUtils.nicePrint(document));
      System.out.println(document2);
      
      
      System.out.print("Enter the sentence: ");
      input = kb.nextLine();
    }
  }
}
