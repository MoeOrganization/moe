use Test::More;

# test that eval is using the enclosing environment
{
    sub foo { "foo" }

    my $r = eval("foo()");
    ok(not($!.defined), "... no expection should have been thrown");
    is($r, "foo", '... got the expected result');

    eval("foobar()");
    ok($!.defined, "... an expection should have been thrown");

    my $r2 = eval("foo()");
    ok(not($!.defined), '... the $! variable has been reset');
    is($r2, "foo", '... got the expected result');
}

# test it can access variables
{
    my $x = 10;

    my $r = eval('$x + 10');
    ok(not($!.defined), "... no expection should have been thrown");
    is($r, 20, '... got the expected value back');
}

# test that it can change variables
{
    my $x = 10;

    my $r = eval('$x = $x + 10');
    ok(not($!.defined), "... no expection should have been thrown");
    is($r, 20, '... got the expected value back');
    is($x, 20, '... got the expected value back');
}

done_testing();