# Notable changes and fixes

# Version 0.4.1

 * Split chunk compilation and instance creation in the compiler interface.

   This allows using a ChunkLoader to compile a chunk once and then create many independent instances of it
   all with different _ENV and libraries loaded. The bytecode is generated once and loaded classes are shared.

# Version 0.4

 * Allow using function return values in table index assignments.
  
   For example `x[a()] = 1` previously reversed the key and value.
 
# Version 0.3

 * Introduced generics for UserData and AbstractFunctions, making it easier to expose Java classes to Lua.
 * Fixed honoring metamethods for user data in pairs/ipairs. (Issue #1)
