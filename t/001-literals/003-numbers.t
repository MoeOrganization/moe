use Test::More;

# test the multiple ways to create an Num
{
    ok(12.325.isa("Num"), "... literal isa Num");

    my $x = 12.325;
    ok($x.isa("Num"), "... variable isa Num");

    my $y = Num.new(12.325);
    ok($y.isa("Num"), "... object instantiation isa Num");   
    is($y, 12.325, "... object instantiation has the value we expected");    
}

# test the underscores
{
    is(10000.001, 10_000.00_1, "... test the _ seperator");
    is(1000000.000001, 1_000_000.000_001, "... test the _ seperator (twice)");
}

# test with raw literal
{
    ok(12.5.isa("Num"), "... this isa Num");
    ok(12.5.defined, "... this is defined");

    is(12.5, 12.5, "... check the value here");

    # mathmatical operators

    is(12.5 + 5, 17.5,     "... test addition");
    is(12.5 + -5, 7.5,     "... test addition (w/ negative number)");
    is(12.5 + 5.5, 18, "... test addition (w/ float)");

    is(12.5 - 5, 7.5,      "... test subtraction");
    is(12.5 - -5, 17.5,    "... test subtraction (w/ negative number)");
    is(12.5 - 5.5, 7,  "... test subtraction (w/ float)");

    is(12.5 * 5, 62.5,     "... test multiplication");
    is(12.5 * -5, -62.5,   "... test multiplication (w/ negative number)");
    is(12.5 * 5.5, 68.75, "... test multiplication (w/float)");

    is(12.5 / 2, 6.25,      "... test division");
    is(12.5 / -2, -6.25,  "... test division (w/ negative number)");
    is(12.5 / 2.5, 5,  "... test division (w/ float)");

    is(12.5 % 5, 2,      "... test modulo");
    is(12.5 % -5, -3,    "... test modulo (w/ negative number)");
    is(12.5 % 5.2, 2,    "... test modulo (w/ float)");

    # exponentiation

    is(12.5 ** 5, 305175.78125, "... test addition");

    # comparison operators

    ok(12.5 == 12.5, "... test equality");
    ok(12.5 != 20.1, "... test inequality");
    ok(12.5 != 12,   "... test inequality (w/ int)");
 
    ok(12.5 < 15.1,  "... test less than");
    ok(12.5 <= 15.1, "... test less than or equal to");
    ok(12.5 <= 12.5, "... test less than or equal to (equal)");

    ok(12.5 < 15,  "... test less than (w/ int)");
    ok(12.5 <= 15, "... test less than or equal to (w/ int)");

    ok(12.5 > 5.2,   "... test greater than");
    ok(12.5 >= 5.2,  "... test greater than or equal to");
    ok(12.5 >= 12.5, "... test greater than or equal to (equal)");

    ok(12.5 > 5,   "... test greater than (w/ int)");
    ok(12.5 >= 5,  "... test greater than or equal to (w/ int)");    

    is(12.5 <=> 12.5, 0,  "... test spaceship")
    is(12.5 <=> 15.1, -1, "... test spaceship (less than)");
    is(12.5 <=> 9.2,  1,  "... test spaceship (greater than)");    

    is(12.5 <=> 15, -1, "... test spaceship (less than) (w/int)");
    is(12.5 <=> 9,  1,  "... test spaceship (greater than) (w/int)");

    # coercion

    is(12.5.Int, 12,    "... coercion to an Int");
    is(12.5.Num, 12.5,  "... coercion to an Num");
    is(12.5.Str, "12.5",  "... coercion to an Str");
    is(12.5.Bool, true, "... coercion to an Bool");

    is(+12.5, 12.5,   "... coercion to an Int");
    is(~12.5, "12.5", "... coercion to an Str");
    is(?12.5, true, "... coercion to an Bool");
}

# test with variable
{
    my $x = 12.5;

    ok($x.isa("Num"), "... this isa Num");
    ok($x.defined, "... this is defined");

    is($x, 12.5, "... check the value here");

    # mathmatical operators

    is($x + 5, 17.5,     "... test addition");
    is($x + -5, 7.5,     "... test addition (w/ negative number)");
    is($x + 5.5, 18, "... test addition (w/ float)");

    is($x - 5, 7.5,      "... test subtraction");
    is($x - -5, 17.5,    "... test subtraction (w/ negative number)");
    is($x - 5.5, 7,  "... test subtraction (w/ float)");

    is($x * 5, 62.5,     "... test multiplication");
    is($x * -5, -62.5,   "... test multiplication (w/ negative number)");
    is($x * 5.5, 68.75, "... test multiplication (w/float)");

    is($x / 2, 6.25,      "... test division");
    is($x / -2, -6.25,  "... test division (w/ negative number)");
    is($x / 2.5, 5,  "... test division (w/ float)");

    is($x % 5, 2,      "... test modulo");
    is($x % -5, -3,    "... test modulo (w/ negative number)");
    is($x % 5.2, 2,    "... test modulo (w/ float)");

    # exponentiation

    is($x ** 5, 305175.78125, "... test addition");

    # comparison operators

    ok($x == 12.5, "... test equality");
    ok($x != 20.1, "... test inequality");
    ok($x != 12,   "... test inequality (w/ int)");
 
    ok($x < 15.1,  "... test less than");
    ok($x <= 15.1, "... test less than or equal to");
    ok($x <= 12.5, "... test less than or equal to (equal)");

    ok($x < 15,  "... test less than (w/ int)");
    ok($x <= 15, "... test less than or equal to (w/ int)");

    ok($x > 5.2,   "... test greater than");
    ok($x >= 5.2,  "... test greater than or equal to");
    ok($x >= 12.5, "... test greater than or equal to (equal)");

    ok($x > 5,   "... test greater than (w/ int)");
    ok($x >= 5,  "... test greater than or equal to (w/ int)");    

    is($x <=> 12.5, 0,  "... test spaceship")
    is($x <=> 15.1, -1, "... test spaceship (less than)");
    is($x <=> 9.2,  1,  "... test spaceship (greater than)");    

    is($x <=> 15, -1, "... test spaceship (less than) (w/int)");
    is($x <=> 9,  1,  "... test spaceship (greater than) (w/int)");

    # coercion

    is($x.Int, 12,    "... coercion to an Int");
    is($x.Num, 12.5,  "... coercion to an Num");
    is($x.Str, "12.5",  "... coercion to an Str");
    is($x.Bool, true, "... coercion to an Bool");

    is(+$x, 12.5,   "... coercion to an Int");
    is(~$x, "12.5", "... coercion to an Str");
    is(?$x, true, "... coercion to an Bool");
}

done_testing();