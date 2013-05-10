use Test::More;

class Point {
    has $!x = 0;
    has $!y = 0;

    method x ($x?) { 
        if ($x) { $!x = $x } 
        $!x 
    }

    method y ($y?) {
        if ($y) { $!y = $y } 
        $!y 
    }

    method clear {
        ($!x, $!y) = (0, 0);
    }
}

class Point3D extends Point {
    has $!z = 0;

    method z ($z?) {
        if ($z) { $!z = $z } 
        $!z 
    }

    method clear {
        super;
        $!z = 0;
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

# test inheritance
{
    my $p3D = Point3D.new;
    ok($p3D.defined, '... the instance is defined');
    ok($p3D.isa("Point3D"),  '... the instance isa Point3D');
    ok($p3D.isa("Point"),  '... the instance isa Point');
    ok($p3D.isa("Any"),    '... the instance is dereived from Any');
    ok($p3D.isa("Object"), '... the instance is dereived from Object');

    is($p3D.x, 0, '... x has the default value');
    is($p3D.y, 0, '... y has the default value');
    is($p3D.z, 0, '... z has the default value');

    is($p3D.x(10), 10, '... setting x worked');
    is($p3D.y(20), 20, '... setting y worked');
    is($p3D.z(30), 30, '... setting z worked');

    is($p3D.x, 10, '... x has the new value');
    is($p3D.y, 20, '... y has the new value');
    is($p3D.z, 30, '... z has the new value');

    $p3D.clear;

    is($p3D.x, 0, '... x is back to the default value');
    is($p3D.y, 0, '... y is back to the default value');
    is($p3D.z, 0, '... z is back to the default value');
}

{
    my $p3D = Point3D.new(x => 10, y => 20, z => 30);
    ok($p3D.defined, '... the instance is defined');
    ok($p3D.isa("Point3D"),  '... the instance isa Point3D');

    is($p3D.x, 10, '... x has the value from the constructor');
    is($p3D.y, 20, '... y has the value from the constructor');
    is($p3D.z, 30, '... z has the value from the constructor');
}

{
    my $p3D = Point3D.new(x => 10);
    ok($p3D.defined, '... the instance is defined');
    ok($p3D.isa("Point3D"),  '... the instance isa Point3D');

    is($p3D.x, 10, '... x has the value from the constructor');
    is($p3D.y, 0, '... y has the default value');
    is($p3D.z, 0, '... z has the default value');
}


done_testing();