use Test::More;

{
    my &id = -> ($x) { $x };
    is(&id.(10), 10, '... implicit call worked as expected');
    is(&id.call(10), 10, '... call worked as expected');
    is(&id.apply([10]), 10, '... apply worked as expected');
}

{
    my &adder = -> ($x, $y) { $x + $y };
    is(&adder.(2, 2), 4, '... multiple args works');
}

{
    my &adder = -> ($x, $y?) { $x + ($y || 10) };
    is(&adder.(2, 2), 4, '... one optional arg works (supply both)');
    is(&adder.(2), 12, '... one optional arg works (supply only one)');
}

{
    my &collector = -> ($x, *@y) { $x + @y.length };
    is(&collector.(1, 2), 2, '... slupry arg works');
    is(&collector.(1, 2, 3, 4, 5), 5, '... slupry arg works (with more)');
}

{
    my &named = -> ($key, *%x) { %x{$key} };
    is(&named.("one", one => 1, two => 2), 1, '... slurpy named works');
}

done_testing();