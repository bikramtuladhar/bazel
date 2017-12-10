// Copyright 2017 The Bazel Authors. All rights reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//    http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.devtools.build.lib.analysis;

import static com.google.common.truth.Truth.assertThat;

import com.google.common.collect.ImmutableMap;
import com.google.devtools.build.lib.packages.AbstractRuleErrorConsumer;
import com.google.devtools.build.lib.packages.RuleErrorConsumer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/** Unit tests for {@link LocationExpander}. */
@RunWith(JUnit4.class)
public class LocationExpanderTest {
  private static final class Capture extends AbstractRuleErrorConsumer
      implements RuleErrorConsumer {
    private final List<String> warnsOrErrors = new ArrayList<>();

    @Override
    public void ruleWarning(String message) {
      warnsOrErrors.add("WARN: " + message);
    }

    @Override
    public void ruleError(String message) {
      warnsOrErrors.add("ERROR: " + message);
    }

    @Override
    public void attributeWarning(String attrName, String message) {
      warnsOrErrors.add("WARN-" + attrName + ": " + message);
    }

    @Override
    public void attributeError(String attrName, String message) {
      warnsOrErrors.add("ERROR-" + attrName + ": " + message);
    }

    @Override
    public boolean hasErrors() {
      return !warnsOrErrors.isEmpty();
    }
  }

  private LocationExpander makeExpander(RuleErrorConsumer ruleErrorConsumer) throws Exception {
    return new LocationExpander(
        ruleErrorConsumer,
        ImmutableMap.<String, Function<String, String>>of(
            "location", (String s) -> "one(" + s + ")",
            "locations", (String s) -> "more(" + s + ")"));
  }

  private String expand(String input) throws Exception {
    return makeExpander(new Capture()).expand(input);
  }

  @Test
  public void noExpansion() throws Exception {
    assertThat(expand("abc")).isEqualTo("abc");
  }

  @Test
  public void oneOrMore() throws Exception {
    assertThat(expand("$(location a)")).isEqualTo("one(a)");
    assertThat(expand("$(locations b)")).isEqualTo("more(b)");
    assertThat(expand("---$(location a)---")).isEqualTo("---one(a)---");
  }

  @Test
  public void twoInOne() throws Exception {
    assertThat(expand("$(location a) $(locations b)")).isEqualTo("one(a) more(b)");
  }

  @Test
  public void notAFunction() throws Exception {
    assertThat(expand("$(locationz a)")).isEqualTo("$(locationz a)");
  }

  @Test
  public void missingClosingParen() throws Exception {
    Capture capture = new Capture();
    String value = makeExpander(capture).expand("foo $(location a");
    // In case of an error, no location expansion is performed.
    assertThat(value).isEqualTo("foo $(location a");
    assertThat(capture.warnsOrErrors).containsExactly("ERROR: unterminated $(location) expression");
  }

  // In case of errors, the exact return value is unspecified. However, we don't want to
  // accidentally change the behavior even in this unspecified case - that's why I added a test
  // here.
  @Test
  public void noExpansionOnError() throws Exception {
    Capture capture = new Capture();
    String value = makeExpander(capture).expand("foo $(location a) $(location a");
    assertThat(value).isEqualTo("foo $(location a) $(location a");
    assertThat(capture.warnsOrErrors).containsExactly("ERROR: unterminated $(location) expression");
  }
}