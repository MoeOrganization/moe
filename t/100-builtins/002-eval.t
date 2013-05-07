use Test::More;

# test that eval is using the enclosing environment
{
    sub foo { "foo" }

    eval("foo()");
    ok(not($!.defined), "... no expection should have been thrown");

    eval("foobar()");
    ok($!.defined, "... an expection should have been thrown");

    eval("foo()");
    ok(not($!.defined), "... the environment has been rest");
}

done_testing();