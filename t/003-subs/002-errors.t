use Test::More;

sub id ($x) { $x }

{
    eval("id()");
    ok($!.defined, '... not enough arguments provided fails correctly');

    eval("id(1, 2)");
    ok($!.defined, '... too many arguments provided fails correctly');

    eval("id(1)");
    ok(not($!.defined), '... right number of arguments succeeds correctly');
}

sub want_array (@a) { @a.length }

{
    eval("want_array(1)");
    ok($!.defined, '... incorrect argument types');

    eval("want_array('foo', 'bar')");
    ok($!.defined, '... incorrect argument types (and too many)');

    eval("want_array({})");
    ok($!.defined, '... incorrect argument types');

    eval("want_array([ 1, 2, 3 ])");
    ok(not($!.defined), '... correct argument types');
}

done_testing();