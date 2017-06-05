-- Author: Michael-Keith Bernard
-- Date: May 22, 2012
-- Description: Various implementations of the Fibonacci sequence in Lua. Lua
-- has native support for tail-call elimination which is why `tail_call` and
-- `continuation` run in near constant time. For sufficiently large numbers of n
-- you can start to see linear performace characteristics (particularly for the
-- `continuation` implementation), but ultimately the `tail_call` implementation
-- is an order of magnitude faster than iteration even for values of n as small
-- as 500k.

Fibonacci = {}

-- Naive recursive
function Fibonacci.naive(n)
  local function inner(m)
    if m < 2 then
      return m
    end
    return inner(m-1) + inner(m-2)
  end
  return inner(n)
end

-- Iterative
function Fibonacci.iterative(n)
  a, b = 0, 1

  for i = 1, n do
    a, b = b, a + b
  end
  return a
end

-- Memoized naive recursive
function Fibonacci.memoized(n)
  local memo = {}
  local function inner(m)
    if m < 2 then
      return m
    end

    if memo[m] then
      return memo[m]
    else
      local res = inner(m-1) + inner(m-2)
      memo[m] = res
      return res
    end
  end
  return inner(n)
end

-- Tail-optimized recursive
function Fibonacci.tail_call(n)
  local function inner(m, a, b)
    if m == 0 then
      return a
    end
    return inner(m-1, b, a+b)
  end
  return inner(n, 0, 1)
end

-- Continuation passing style
function Fibonacci.continuation(n)
  local function inner(m, cont)
    if m == 0 then
      return cont(0, 1)
    end
    return inner(m-1, function(a, b) return cont(b, a+b) end)
  end
  return inner(n, function(a, b) return a end)
end

function timeit(f, ...)
  local start = os.time()
  local res = { f(...) }
  local delta = os.time() - start

  return delta, table.unpack(res)
end

for _, n in ipairs({10, 25, 35, 100, 1000, 5000, 100000, 1000000, 5000000}) do
  print("Fib of "..n)
  for name, fibfun in pairs(Fibonacci) do
    if name == "naive" and n > 35 then
      print(string.format("  %s, time: %s, %s", name, "skipped", "skipped"))
    elseif name == "memoized" and n > 5000 then
      print(string.format("  %s, time: %s, %s", name, "skipped", "skipped"))
    else
      print(string.format("  %s, time: %s, %s", name, timeit(fibfun, n)))
    end
  end
end
