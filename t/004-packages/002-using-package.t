use Test::More;

@INC.push("t/004-packages/lib");

eval("use Bar;");

eval("baz()");
ok(not($!.defined), '... the baz subroutine got exported');

eval("foo()");
ok($!.defined, '... the foo subroutine did not get exported');

is(baz(), "baz", '... got the expected value from exported baz');
is(Bar::foo(), "Bar::foo", '... got the expected value from the unexported foo');

done_testing();