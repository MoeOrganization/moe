use Test::More;

ok(true, "... this works");
is(5, 5, "... this works too");
is(5, '5', "... and this works too");
is("foo", 'foo', "... and so does this");

done_testing();