use Test::More;

ok(true, "... this works");
is(5, 5, "... this works too");
is(5, '5', "... and this works too");
is("foo", 'foo', "... and so does this");
is(true, true, "... and so does this");

is_deeply([1, 2, 3], [1, 2, 3], "... this works");
is_deeply({ one => 1 }, { one => 1 }, "... this works");

done_testing();