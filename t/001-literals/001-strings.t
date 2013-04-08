use Test::More;

# test with raw literal
{
    ok("foO".isa("Str"), "... this isa Str");
    ok("foO".defined, "... the string is defined");

    # informational

    is("foO".length, 3, "... the string is 3 characters long");

    # transformers

    is("foO".uc,      "FOO", "... this is the string we expected (uc)");
    is("foO".ucfirst, "FoO", "... this is the string we expected (ucfirst)");
    is("foO".lc,      "foo", "... this is the string we expected (lc)");
    is("foO".lcfirst, "foO", "... this is the string we expected (lcfirst)");
    is("foO".reverse, "Oof", "... this is the string we expected (reverse)");

    my @split = "foO".split("o");
    is(@split[0], "f", "... got the array of strings we expected (split)");
    is(@split[1], "O", "... got the array of strings we expected (split)");

    is("foO".concat("bAr"), "foObAr", "... got the string we expected (concat)");
    is(("foO" ~ "bAr"), "foObAr", "... got the string we expected (~)");

    is(("foO" x 3), "foOfoOfoO", "... got the string we expected (x)");

    # coercions

    ok(?"foO",     "... the string is true");
    ok("foO".Bool, "... the string is true");

    is(~"foO",     "foO", "... string coercion returns the same string");
    is("foO".Str,  "foO", "... string coercion returns the same string");

    is(+"foO",     0.0, "... number coercion returns 0.0");
    is("foO".Num,  0.0, "... number coercion returns 0.0");
    is("foO".Int,  0,   "... integer coercion returns 0");

    # logical operators 

    ok("foO" eq "foO", "... eq operator works");
    ok("foO" ne "FoO", "... ne operator works");
    ok("foO" gt "eee", "... gt operator works");
    ok("foO" ge "foO", "... ge operator works (equal)");
    ok("foO" ge "eee", "... ge operator works (not equal)");
    ok("foO" lt "foo", "... lt operator works");
    ok("foO" le "foO", "... le operator works (equal)");
    ok("foO" le "foo", "... le operator works (not equal)");

    is("foO" cmp "foO", 0,  "... cmp operator works (equal)");
    is("foO" cmp "eee", 1,  "... cmp operator works (greater than)");
    is("foO" cmp "foo", -1, "... cmp operator works (less than)");
}

# test with variable
{
    my $x = "foO";

    ok($x.isa("Str"), "... this isa Str");
    ok($x.defined, "... the string is defined");

    # informational

    is($x.length, 3, "... the string is 3 characters long");

    # transformers

    is($x,         "foO", "... this is the string we expected");
    is($x.uc,      "FOO", "... this is the string we expected (uc)");
    is($x.ucfirst, "FoO", "... this is the string we expected (ucfirst)");
    is($x.lc,      "foo", "... this is the string we expected (lc)");
    is($x.lcfirst, "foO", "... this is the string we expected (lcfirst)");
    is($x.reverse, "Oof", "... this is the string we expected (reverse)");

    my @split = $x.split("o");
    is(@split[0], "f", "... got the array of strings we expected (split)");
    is(@split[1], "O", "... got the array of strings we expected (split)");

    is($x.concat("bAr"), "foObAr", "... got the string we expected (concat)");
    is(($x ~ "bAr"), "foObAr", "... got the string we expected (~)");

    is(($x x 3), "foOfoOfoO", "... got the string we expected (x)");

    # coercions

    ok(?$x,     "... the string is true");
    ok($x.Bool, "... the string is true");

    is(~$x,     "foO", "... string coercion returns the same string");
    is($x.Str,  "foO", "... string coercion returns the same string");

    is(+$x,     0.0, "... number coercion returns 0.0");
    is($x.Num,  0.0, "... number coercion returns 0.0");
    is($x.Int,  0,   "... integer coercion returns 0");

    # logical operators 

    ok($x eq "foO", "... eq operator works");
    ok($x ne "FoO", "... ne operator works");
    ok($x gt "eee", "... gt operator works");
    ok($x ge "foO", "... ge operator works (equal)");
    ok($x ge "eee", "... ge operator works (not equal)");
    ok($x lt "foo", "... lt operator works");
    ok($x le "foO", "... le operator works (equal)");
    ok($x le "foo", "... le operator works (not equal)");

    is($x cmp "foO", 0,  "... cmp operator works (equal)");
    is($x cmp "eee", 1,  "... cmp operator works (greater than)");
    is($x cmp "foo", -1, "... cmp operator works (less than)");
}

done_testing();

