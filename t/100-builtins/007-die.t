use Test::More;

{
    eval("die('foo')");
    ok($!.defined, '... the exception was created properly');
    is($!.msg, 'org.moe.runtime.MoeErrors$MoeProblems: foo', '... got the right error message');
}

done_testing();