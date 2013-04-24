# P24 (*) Lotto: Draw N different random numbers from the set 1..M.
#    Example:

#    moe> lotto(6, 49)
#    [23, 1, 17, 33, 21, 37]

use Test::More;

# from P20
sub remove_at($i, @list) {
    my $removed = @list[$i];
    my @rest = @list[0 .. ($i-1)];
    @rest.push(@list[($i+1) .. (@list.length-1)]);
    [@rest.flatten, $removed]
}

# from P23
sub random_select($n, @list) {
    my @l = @list;
    1..$n.map(-> {my @result = remove_at(rand(@l.length).Int, @l); @l = @result[0]; @result[1]})
}

sub lotto($n, $m) { random_select($n, 1..$m) }

my @picks = lotto(6, 49);
say @picks;

is(@picks.length, 6, "... P24 -- number of picks");
ok(@picks.uniq.length == @picks.length, "... P24 -- no duplicates");
0..5.each(-> ($i) { ok(@picks[$i] > 0 && @picks[$i] <= 49, "... P24 -- pick " ~ ~($i + 1)) });

done_testing();

