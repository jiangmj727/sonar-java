package checks.regex;

public class ImpossibleBoundariesCheck {

  void noncompliant(String str) {
    // Noncompliant@+1 [[sc=18;ec=19]] {{Remove or replace this boundary that will never match because it appears before mandatory input.}}
    str.matches("$[a-z]^"); // Noncompliant [[sc=24;ec=25]] {{Remove or replace this boundary that will never match because it appears after mandatory input.}}
    str.matches("$[a-z]"); // Noncompliant [[sc=18;ec=19]] {{Remove or replace this boundary that will never match because it appears before mandatory input.}}
    str.matches("$(abc)"); // Noncompliant [[sc=18;ec=19]]
    str.matches("[a-z]^"); // Noncompliant [[sc=23;ec=24]]
    str.matches("\\Z[a-z]"); // Noncompliant [[sc=18;ec=21]]
    str.matches("\\z[a-z]"); // Noncompliant [[sc=18;ec=21]]
    str.matches("[a-z]\\A"); // Noncompliant [[sc=23;ec=26]]
    str.matches("($)a"); // Noncompliant [[sc=19;ec=20]]
    str.matches("a$|$a"); // Noncompliant [[sc=21;ec=22]]
    str.matches("^a|a^"); // Noncompliant [[sc=22;ec=23]]
    str.matches("a(b|^)"); // Noncompliant [[sc=22;ec=23]]
    str.matches("(?=abc^)"); // Noncompliant [[sc=24;ec=25]]
    str.matches("(?:abc(X|^))*Y?"); // Noncompliant [[sc=27;ec=28]]
  }

  void compliant(String str) {
    str.matches("^[a-z]$");
    str.matches("^$");
    str.matches("^(?i)$");
    str.matches("^$(?i)");
    str.matches("^abc$|^def$");
    str.matches("(?i)^abc$");
    str.matches("()^abc$");
    str.matches("^abc$()");
    str.matches("^abc$\\b");
    str.matches("(?=abc)^abc$");
    str.matches("(?=$)");
    str.matches("(?i)(true)(?=(?:[^']|'[^']*')*$)");
    str.matches("(?:abc(X|$))*Y?");
    str.matches("$()*");
  }

}
