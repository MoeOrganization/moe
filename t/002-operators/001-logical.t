use Test::More;

{
    ok(true && true, '... && operator');
    ok(false || true, '... || operator');
    ok(true && ( false || true ), '... && and || combined');

    is(0 || "hello", "hello", '... return value of ||');
    is("hang" && 10, 10, '... return value of &&');

    is(((true) ? "true" : "false"), "true", '... ?: operator');
}

done_testing();