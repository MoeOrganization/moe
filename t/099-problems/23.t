# P23 (**) Extract a given number of randomly selected elements from a list.
#     Example:

#     moe> random_select(3, ['a', 'b', 'c', 'd', 'f', 'g', 'h'])
#     ['e', 'd', 'a']

# Note: the selection is done without replacement; there should be no
# duplicates in output

use Test::More;

# from P20
sub remove_at($i, @list) {
    my $removed = @list[$i];
    my @rest = @list[0 .. ($i-1)];
    @rest.push(@list[($i+1) .. (@list.length-1)]);
    [@rest.flatten, $removed]
}

sub random_select($n, @list) {
    my @l = @list;
    1..$n.map(-> {
                     my @result = remove_at(rand(@l.length).Int, @l);
                     @l = @result[0];
                     @result[1]
                 })
}

my @picks = random_select(3, ['a', 'b', 'c', 'd', 'f', 'g', 'h']);

# can't really test for specific values in the result
is(@picks.length, 3, "... P23");

# check for duplicates
ok(@picks.uniq.length == @picks.length, "... P23 -- no duplicates");

done_testing();
