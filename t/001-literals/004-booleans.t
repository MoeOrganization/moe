use Test::More;

# test the raw literal
{
    ok(true, "... true is true");
    ok(not(false), "... false is false");

    ok(true.defined, "... true is defined");
    ok(false.defined, "... false is defined");

    ok(true.isa("Bool"), "... true isa Bool");
    ok(false.isa("Bool"), "... false isa Bool");

    ok(true == true, "... true is equal to true");
    ok(true != false, "... true is not equal to false");    

    ok(false == false, "... false is equal to false");
    ok(false != true, "... false is not equal to true");   

    # corecions
    is(true.Int,  1, "... got the right Int coercion for true");
    is(false.Int, 0, "... got the right Int coercion for false"); 

    is(true.Num,  1.0, "... got the right Num coercion for true");
    is(false.Num, 0.0, "... got the right Num coercion for false"); 

    is(true.Str,  "true",  "... got the right Str coercion for true");
    is(false.Str, "false", "... got the right Str coercion for false"); 

    is(+true,  1, "... coercion to Int (w/ operator)");
    is(+false, 0, "... coercion to Int (w/ operator)");

    is(~true,  "true",  "... coercion to Str (w/ operator)");
    is(~false, "false", "... coercion to Str (w/ operator)");

    is(?true,  true, "... coercion to Bool (w/ operator)");
    is(?false, false, "... coercion to Bool (w/ operator)");
}

# test with variables
{
    my $t = true;
    my $f = false;

    ok($t, "... true is true");
    ok(not($f), "... false is false");

    ok($t.defined, "... true is defined");
    ok($f.defined, "... false is defined");

    ok($t.isa("Bool"), "... true isa Bool");
    ok($f.isa("Bool"), "... false isa Bool");

    ok($t == $t, "... true is equal to true");
    ok($t != $f, "... true is not equal to false");    

    ok($f == $f, "... false is equal to false");
    ok($f != $t, "... false is not equal to true"); 

    # corecions
    is($t.Int,  1, "... got the right Int coercion for true");
    is($f.Int, 0, "... got the right Int coercion for false"); 

    is($t.Num,  1.0, "... got the right Num coercion for true");
    is($f.Num, 0.0, "... got the right Num coercion for false"); 

    is($t.Str,  "true",  "... got the right Str coercion for true");
    is($f.Str, "false", "... got the right Str coercion for false"); 

    is(+$t,  1, "... coercion to Int (w/ operator)");
    is(+$f, 0, "... coercion to Int (w/ operator)");

    is(~$t,  "true",  "... coercion to Str (w/ operator)");
    is(~$f, "false", "... coercion to Str (w/ operator)");

    is(?$t,  true, "... coercion to Bool (w/ operator)");
    is(?$f, false, "... coercion to Bool (w/ operator)");   
}

# test some simple expressions
{
    my $b1 = 10 == 10;
    my $b2 = "foo" eq "bar";
    my $b3 = 12.5 != 20;

    ok($b1.isa("Bool"), "... expression resulted in a Bool");
    ok($b2.isa("Bool"), "... expression resulted in a Bool");
    ok($b3.isa("Bool"), "... expression resulted in a Bool");
}

done_testing();