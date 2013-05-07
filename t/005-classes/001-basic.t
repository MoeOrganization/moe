use Test::More;

class Point {
    has $!x = 0;
    has $!y = 0;

    method x ($x?) { 
        if ($x) { $!x = $x; } 
        $!x 
    }
    method y ($y?) {
        if ($y) { $!y = $y; } 
        $!y 
    }

    method clear {
        ($!x, $!y) = (0, 0);
    }
}

{
    my $p = Point.new;
    ok($p.defined, '... the instance is defined');
    ok($p.isa("Point"),  '... the instance isa Point');
    ok($p.isa("Any"),    '... the instance is dereived from Any');
    ok($p.isa("Object"), '... the instance is dereived from Object');

    is($p.x, 0, '... x has the default value');
    is($p.y, 0, '... y has the default value');

    is($p.x(10), 10, '... setting x worked');
    is($p.y(20), 20, '... setting y worked');

    is($p.x, 10, '... x has the new value');
    is($p.y, 20, '... y has the new value');

    $p.clear;

    is($p.x, 0, '... x is back to the default value');
    is($p.y, 0, '... y is back to the default value');
}

{
    my $p = Point.new(x => 10, y => 20);
    ok($p.defined, '... the instance is defined');
    ok($p.isa("Point"),  '... the instance isa Point');

    is($p.x, 10, '... x has the value from the constructor');
    is($p.y, 20, '... y has the value from the constructor');
}

{
    my $p = Point.new(x => 10);
    ok($p.defined, '... the instance is defined');
    ok($p.isa("Point"),  '... the instance isa Point');

    is($p.x, 10, '... x has the value from the constructor');
    is($p.y, 0, '... y has the default value');
}


done_testing();