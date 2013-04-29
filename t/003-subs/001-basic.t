use Test::More;

sub id ($x) { $x }
sub adder ($x, $y) { $x + $y }
sub adder_w_optional ($x, $y?) { $x + ($y || 10) }
sub collector ($x, *@y) { $x + @y.length }
sub named ($key, *%x) { %x{$key} }

is(id(10), 10, '... calling id() worked');
is(adder(2, 2), 4, '... calling adder() worked');

is(adder_w_optional(2, 2), 4, '... calling adder_w_optional() worked (supply both)');
is(adder_w_optional(2), 12, '... calling adder_w_optional() worked (supply only one)');

is(collector(1, 2), 2, '... calling collector() w/ slupry args worked');
is(collector(1, 2, 3, 4, 5), 5, '... calling collector() w/ slupry args worked (with more args)');

is(named("one", one => 1, two => 2), 1, '... calling named() w/ slurpy named args worked');

{
    is(&id.call(10), 10, '... &id.call worked as expected');
    is(&id.apply([10]), 10, '... &id.apply worked as expected');
}

done_testing();