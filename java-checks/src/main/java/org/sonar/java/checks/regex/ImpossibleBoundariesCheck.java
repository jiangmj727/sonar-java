/*
 * SonarQube Java
 * Copyright (C) 2012-2020 SonarSource SA
 * mailto:info AT sonarsource DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.java.checks.regex;

import java.util.Collections;
import org.sonar.check.Rule;
import org.sonar.java.checks.helpers.RegexTreeHelper;
import org.sonar.java.regex.RegexParseResult;
import org.sonar.java.regex.ast.AutomatonState;
import org.sonar.java.regex.ast.BoundaryTree;
import org.sonar.java.regex.ast.RegexBaseVisitor;
import org.sonar.plugins.java.api.tree.MethodInvocationTree;

@Rule(key = "S5996")
public class ImpossibleBoundariesCheck extends AbstractRegexCheck {

  private static final String MESSAGE = "Remove or replace this boundary that will never match because it appears %s mandatory input.";

  @Override
  public void checkRegex(RegexParseResult regexForLiterals, MethodInvocationTree mit) {
    new ImpossibleBoundaryFinder().visit(regexForLiterals);
  }

  private class ImpossibleBoundaryFinder extends RegexBaseVisitor {

    private AutomatonState startOfRegex;
    private AutomatonState endOfRegex;

    @Override
    public void visit(RegexParseResult regexParseResult) {
      startOfRegex = regexParseResult.getStartState();
      endOfRegex = regexParseResult.getFinalState();
      super.visit(regexParseResult);
    }

    @Override
    public void visitBoundary(BoundaryTree boundaryTree) {
      switch (boundaryTree.type()) {
        case LINE_START:
        case INPUT_START:
          if (!RegexTreeHelper.canReachWithoutConsumingInput(startOfRegex, boundaryTree)) {
            reportIssue(boundaryTree, String.format(MESSAGE, "after"), null, Collections.emptyList());
          }
          break;
        case LINE_END:
        case INPUT_END:
        case INPUT_END_FINAL_TERMINATOR:
          if (!RegexTreeHelper.canReachWithoutConsumingInput(boundaryTree, endOfRegex)) {
            reportIssue(boundaryTree, String.format(MESSAGE, "before"), null, Collections.emptyList());
          }
          break;
        default:
          // Do nothing
      }
    }
  }

}
