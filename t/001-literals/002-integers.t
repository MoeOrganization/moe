use Test::More;

# test the multiple ways to create an Int
{
    ok(10.isa("Int"), "... literal isa Int");

    my $x = 10;
    ok($x.isa("Int"), "... variable isa Int");

    my $y = Int.new(10);
    ok($y.isa("Int"), "... object instantiation isa Int");   
    is($y, 10, "... object instantiation has the value we expected");    
}

# test the underscores
{
    is(10000, 10_000, "... test the _ seperator");
    is(1000000, 1_000_000, "... test the _ seperator (twice)");
}

# test with raw literal
{
    ok(10.isa("Int"), "... this isa Int");
    ok(10.defined, "... this is defined");

    is(10, 10, "... check the value here");

    # mathmatical operators

    is(10 + 5, 15,     "... test addition");
    is(10 + -5, 5,     "... test addition (w/ negative number)");
    is(10 + 5.5, 15.5, "... test addition (w/ float)");

    is(10 - 5, 5,      "... test subtraction");
    is(10 - -5, 15,    "... test subtraction (w/ negative number)");
    is(10 - 5.5, 4.5,  "... test subtraction (w/ float)");

    is(10 * 5, 50,     "... test multiplication");
    is(10 * -5, -50,   "... test multiplication (w/ negative number)");
    is(10 * 5.5, 55.0, "... test multiplication (w/float)");

    is(10 / 2, 5,      "... test division");
    is(10 / -2, -5.0,  "... test division (w/ negative number)");
    is(10 / 2.5, 4.0,  "... test division (w/ float)");

    is(10 % 3, 1,      "... test modulo");
    is(10 % -3, -2,    "... test modulo (w/ negative number)");
    is(10 % 3.5, 1,    "... test modulo (w/ float)");

    # exponentiation

    is(10 ** 5, 100_000, "... test addition");

    # comparison operators

    ok(10 == 10,   "... test equality");
    ok(10 == 10.0, "... test equality (w/ float)");
    ok(10 != 20,   "... test inequality");
    ok(10 != 10.5, "... test inequality (w/ float)");
 
    ok(10 < 15,  "... test less than");
    ok(10 <= 15, "... test less than or equal to");
    ok(10 <= 10, "... test less than or equal to (equal)");

    ok(10 < 15.5,  "... test less than (w/ float)");
    ok(10 <= 15.5, "... test less than or equal to (w/ float)");
    ok(10 <= 10.0, "... test less than or equal to (equal) (w/ float)");    

    ok(10 > 5,   "... test greater than");
    ok(10 >= 5,  "... test greater than or equal to");
    ok(10 >= 10, "... test greater than or equal to (equal)");

    ok(10 > 5.2,   "... test greater than (w/ float)");
    ok(10 >= 5.2,  "... test greater than or equal to (w/ float)");
    ok(10 >= 10.0, "... test greater than or equal to (equal) (w/ float)");    

    is(10 <=> 10, 0,  "... test spaceship");
    is(10 <=> 11, -1, "... test spaceship (less than)");
    is(10 <=> 9,  1,  "... test spaceship (greater than)");

    is(10 <=> 10.0, 0,  "... test spaceship (w/ float)");
    is(10 <=> 11.1, -1, "... test spaceship (less than) (w/ float)");
    is(10 <=> 9.2,  1,  "... test spaceship (greater than) (w/ float)");    

    # bitwise

    is(10 & 5, 0,    "... test & bitwise");
    is(10 | 5, 15,   "... test | bitwise");
    is(10 ^ 5, 15,   "... test ^ bitwise");
    is(10 << 5, 320, "... test << bitwise");
    is(10 >> 5, 0,   "... test >> bitwise");

    # methods

    is(10.abs, 10, "... test abs");
    is(10.sin, -0.5440211108893698, "... test sin");
    is(10.cos, -0.8390715290764524, "... test cos");
    is(10.tan, 0.6483608274590866, "... test tan");
    is(10.atan, 1.4711276743037347, "... test atan");
    is(10.exp, 22026.465794806718, "... test exp");
    is(10.log, 2.302585092994046, "... test log");
    is(10.sqrt, 3.1622776601683795, "... test sqrt");

    # coercion

    is(10.Int, 10,    "... coercion to an Int");
    is(10.Num, 10.0,  "... coercion to an Num");
    is(10.Str, "10",  "... coercion to an Str");
    is(10.Bool, true, "... coercion to an Bool");

    is(+10, 10,   "... coercion to an Int");
    is(~10, "10", "... coercion to an Str");
    is(?10, true, "... coercion to an Bool");
}

# test with variable
{
    my $x = 10;

    ok($x.isa("Int"), "... this isa Int");
    ok($x.defined, "... this is defined");

    is($x, 10, "... check the value here");

    # mathmatical operators

    is($x + 5, 15,     "... test addition");
    is($x + -5, 5,     "... test addition (w/ negative number)");
    is($x + 5.5, 15.5, "... test addition (w/ float)");

    is($x - 5, 5,      "... test subtraction");
    is($x - -5, 15,    "... test subtraction (w/ negative number)");
    is($x - 5.5, 4.5,  "... test subtraction (w/ float)");

    is($x * 5, 50,     "... test multiplication");
    is($x * -5, -50,   "... test multiplication (w/ negative number)");
    is($x * 5.5, 55.0, "... test multiplication (w/float)");

    is($x / 2, 5,      "... test division");
    is($x / -2, -5.0,  "... test division (w/ negative number)");
    is($x / 2.5, 4.0,  "... test division (w/ float)");

    is($x % 3, 1,      "... test modulo");
    is($x % -3, -2,    "... test modulo (w/ negative number)");
    is($x % 3.5, 1,    "... test modulo (w/ float)");

    # exponentiation

    is($x ** 5, 100_000, "... test addition");

    # comparison operators

    ok($x == 10,   "... test equality");
    ok($x == 10.0, "... test equality (w/ float)");
    ok($x != 20,   "... test inequality");
    ok($x != 10.5, "... test inequality (w/ float)");
 
    ok($x < 15,  "... test less than");
    ok($x <= 15, "... test less than or equal to");
    ok($x <= 10, "... test less than or equal to (equal)");

    ok($x < 15.5,  "... test less than (w/ float)");
    ok($x <= 15.5, "... test less than or equal to (w/ float)");
    ok($x <= 10.0, "... test less than or equal to (equal) (w/ float)");    

    ok($x > 5,   "... test greater than");
    ok($x >= 5,  "... test greater than or equal to");
    ok($x >= 10, "... test greater than or equal to (equal)");

    ok($x > 5.2,   "... test greater than (w/ float)");
    ok($x >= 5.2,  "... test greater than or equal to (w/ float)");
    ok($x >= 10.0, "... test greater than or equal to (equal) (w/ float)");    

    is($x <=> 10, 0,  "... test spaceship");
    is($x <=> 11, -1, "... test spaceship (less than)");
    is($x <=> 9,  1,  "... test spaceship (greater than)");

    is($x <=> 10.0, 0,  "... test spaceship (w/ float)");
    is($x <=> 11.1, -1, "... test spaceship (less than) (w/ float)");
    is($x <=> 9.2,  1,  "... test spaceship (greater than) (w/ float)");    

    # bitwise

    is($x & 5, 0,    "... test & bitwise");
    is($x | 5, 15,   "... test | bitwise");
    is($x ^ 5, 15,   "... test ^ bitwise");
    is($x << 5, 320, "... test << bitwise");
    is($x >> 5, 0,   "... test >> bitwise");

    # methods

    is($x.abs, 10, "... test abs");
    is($x.sin, -0.5440211108893698, "... test sin");
    is($x.cos, -0.8390715290764524, "... test cos");
    is($x.tan, 0.6483608274590866, "... test tan");
    is($x.atan, 1.4711276743037347, "... test atan");
    is($x.exp, 22026.465794806718, "... test exp");
    is($x.log, 2.302585092994046, "... test log");
    is($x.sqrt, 3.1622776601683795, "... test sqrt");

    # coercion

    is($x.Int, 10,    "... coercion to an Int");
    is($x.Num, 10.0,  "... coercion to an Num");
    is($x.Str, "10",  "... coercion to an Str");
    is($x.Bool, true, "... coercion to an Bool");

    is(+$x, 10,   "... coercion to an Int");
    is(~$x, "10", "... coercion to an Str");
    is(?$x, true, "... coercion to an Bool");
}


done_testing();