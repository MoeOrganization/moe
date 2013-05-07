use Test::More;

sub id ($x) { $x }

{
    is(id(10), 10, '... calling id() worked');
    is(&id.call(10), 10, '... &id.call worked as expected');
    is(&id.apply([10]), 10, '... &id.apply worked as expected');
}

sub adder ($x, $y) { $x + $y }

{
    is(adder(2, 2), 4, '... calling adder() worked');
}

sub adder_w_optional ($x, $y?) { $x + ($y || 10) }

{
    is(adder_w_optional(2, 2), 4, '... calling adder_w_optional() worked (supply both)');
    is(adder_w_optional(2), 12, '... calling adder_w_optional() worked (supply only one)');
}

sub collector ($x, *@y) { $x + @y.length }

{
    is(collector(1, 2), 2, '... calling collector() w/ slupry args worked');
    is(collector(1, 2, 3, 4, 5), 5, '... calling collector() w/ slupry args worked (with more args)');
    is(collector(1, [1, 2, 3]), 2, '... calling collector() with array as second arg');
}

sub named ($key, *%x) { %x{$key} }

{
    is(named("one", one => 1, two => 2), 1, '... calling named() w/ slurpy named args worked');
    is(named("two", one => 1, two => 2), 2, '... calling named() w/ slurpy named args worked');
}

done_testing();