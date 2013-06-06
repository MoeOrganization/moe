# P25 (*) Generate a random permutation of the elements of a list.
#       Hint: Use the solution of problem P23.

#   Example:

#   moe> random_permute(['a', 'b', 'c', 'd', 'e', 'f'])
#   ['b', 'a', 'd', 'c', 'e', 'f']

use Test::More;

# from P20
sub remove_at($i, @list) {
    my $removed = @list[$i];
    my @rest = @list[0 .. ($i-1)];
    @rest.push(@list[($i+1) .. (@list.length-1)]);
    [@rest.flatten, $removed]
}

sub random_permute(@list) {
    my @l = @list;
    1..(@l.length).map(() => {
                            if (@l.length > 1) {
                                my @result = remove_at(rand(@l.length).Int, @l);
                                @l = @result[0];
                                @result[1]
                            }
                            else {
                                @l[0]
                            }
                        })
}

my @list = ['a', 'b', 'c', 'd', 'e', 'f'];
my @permuted = random_permute(@list);

is(@permuted.length, @list.length, "... P25");
@permuted.each(($x) => { ok(@list.exists($x), "... P25 -- exists " ~ $x.Str) });

done_testing();
