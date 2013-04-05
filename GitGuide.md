Git Guide
=========

What is this guide about?
-------------------------

The purpose of this guide is to help you understand how to use Git and GitHub
effectively when working on Moe, using the conventions we find most
comfortable.

We'll cover branches, pulls, pushes, rebasing, and so on, one step at
a time.


Development workflow
--------------------

Once you have a local clone of the repository you can start contributing.
A typical workflow (described in more detail below) is:

  * create a branch to work in
  * while working, keep your local repo in sync with the main repo
  * optionally merge your branch into your local master
  * submit your changes


### cloning your repository

If you have a commit bit on the main repository, you can simply clone that:

    git clone git@github.com:MoeOrganization/moe.git

and your "origin" will be the main Moe repo.

If you do not have a commit bit, fork the project using
the `Fork` button on the Moe project page. Then clone your
fork:

```bash
git clone git@github.com:YOUR_USERNAME/moe.git

# and now to add the original path as the upstream
git remote add upstream https://github.com/MoeOrganization/moe.git
```

You should now have a directory called "moe" containing the entire project,
including all the commit history. (Read up on other Git commands
such as `git log` if you want to review the history.)


### branching

In order to keep your contribution isolated from other code or
documentation changes, it's usually best to create a branch for
it. This makes it easier to review, approve, merge or even revise it,
if necessary.

    git checkout -b my-changes master

This creates and switches you to a new branch called "my-changes"
which is based on the "master" branch (the main project branch and
where all works gets merged eventually).


### committing changes

Once you've created any files you wish to create and have edited whatever
content needs editing, you're ready to add and commit to your branch:

```bash
vi Guide.md       # A new file; We'll need to add it in a moment.
git add Guide.md  # Add to what will be our next commit.
git commit -m "created a new guide"

cd src/main/scala/org/moe/
vi Moe.scala  # Edit a tracked file.
git add -p    # Interactively add changes.
git commit -m "cleaned up some Scala styling"
```

Note that `git add` and `git commit` are commands that have layers of
complexity and features that will not be covered here and can be used in various
different ways that provide a more fluent work-flow coupled with advanced
features. Should you find the time, read more on these commands, especially
`git add -p`, which provides finer-grained control over content adding and
allows to review your work more easily before committing.


### rebasing in preparation for submission

So you finished all your work and it's sitting happily in your local commit
history. Are you sure it's ready for merge?

While you were working it's possible (and sometimes likely) that others have
done work as well. Perhaps some of that work was just merged to the main
branch ("master") of the project. This means that the project has had changes
introduced since you created your branch and - depending on how you look it -
either you're out of step with it, or it is out of step with you.

While this doesn't seem scary, it becomes quite the little annoyance if
you're depending on something that has changed: tests might break, code
might stop working, and merging itself might cause a conflict because two
people might have changed the same content.

What do we do? Rebase!

The act of rebasing is to get your code up to speed with changes that already
occured in the project after you created your branch. Rebasing places your
changes fresh on top of the existing ones.

    # first, we go to the main branch
    git checkout master

    # if you have a commit bit, you simply pull the last changes
    git pull origin master

    # if you work on your own GitHub fork, we pull from upstream
    git pull upstream master

    # now we can go back to our branch and rebase from master
    git checkout my-changes
    git rebase master

Now your code is sitting on top of all the changes that had happened since
you started working on it. Good job!

### submitting your contribution

It's always good manners to submit your contribution for approval instead of
pushing it directly, unless you've gained enough knowledge and experience that
it becomes unlikely you'll make changes that need reverting.

Since we have everything in a branch, we can simply push that branch remotely.

    git push origin my-changes

Now we need to alert the other developers that there's a new branch for review.
This is best done using the "Pull Request" button available on the Github
project page. Click it and follow the instructions.

