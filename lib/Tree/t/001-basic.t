
use Tree;

my $t = ^Tree::NAry.new(node => "0.0", children => [
    ^Tree::NAry.new(node => "1.0", children => [
        ^Tree::NAry.new(node => "1.1"),
        ^Tree::NAry.new(node => "1.2", children => [
            ^Tree::NAry.new(node => "1.2.1"),
            ^Tree::NAry.new(node => "1.2.2"),
        ])
    ]),
    ^Tree::NAry.new(node => "2.0", children => [
        ^Tree::NAry.new(node => "2.1", children => [
            ^Tree::NAry.new(node => "2.1.1", children => [
                ^Tree::NAry.new(node => "2.1.1.1"),
            ]),
            ^Tree::NAry.new(node => "2.1.2", children => [
                ^Tree::NAry.new(node => "2.1.2.1"),
                ^Tree::NAry.new(node => "2.1.2.2"),
            ])
        ])
    ]),
    ^Tree::NAry.new(node => "3.0"),
]);

$t.traverse(-> ($c) {
   say( (".." x $c.depth), $c.node )
});

