use Test::More;

# test the raw literal
{
    ok((foo => 10).isa("Pair"), "... this isa Pair");
    ok((foo => 10).defined, "... this is defined");

    is((foo => 10).key,   "foo", "... got the key we expected");
    is((foo => 10).value, 10,    "... got the value we expected");

    my @kv = (foo => 10).kv;
    is(@kv[0], "foo", "... got the key we expected (from kv method)");
    is(@kv[1], 10,    "... got the value we expected (from kv method)");
}

# test a variable
{
    my $p = foo => 10;

    ok($p.isa("Pair"), "... this isa Pair");
    ok($p.defined, "... this is defined");

    is($p.key,   "foo", "... got the key we expected");
    is($p.value, 10,    "... got the value we expected");

    my @kv = $p.kv;
    is(@kv[0], "foo", "... got the key we expected (from kv method)");
    is(@kv[1], 10,    "... got the value we expected (from kv method)");
}

done_testing();