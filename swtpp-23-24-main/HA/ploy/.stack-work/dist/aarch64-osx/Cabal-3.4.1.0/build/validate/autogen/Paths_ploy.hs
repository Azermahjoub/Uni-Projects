{-# LANGUAGE CPP #-}
{-# LANGUAGE NoRebindableSyntax #-}
{-# OPTIONS_GHC -fno-warn-missing-import-lists #-}
{-# OPTIONS_GHC -Wno-missing-safe-haskell-mode #-}
module Paths_ploy (
    version,
    getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir,
    getDataFileName, getSysconfDir
  ) where

import qualified Control.Exception as Exception
import Data.Version (Version(..))
import System.Environment (getEnv)
import Prelude

#if defined(VERSION_base)

#if MIN_VERSION_base(4,0,0)
catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
#else
catchIO :: IO a -> (Exception.Exception -> IO a) -> IO a
#endif

#else
catchIO :: IO a -> (Exception.IOException -> IO a) -> IO a
#endif
catchIO = Exception.catch

version :: Version
version = Version [1,0,0,0] []
bindir, libdir, dynlibdir, datadir, libexecdir, sysconfdir :: FilePath

bindir     = "/Users/azermahjoub/SWTPP/HA/ploy/.stack-work/install/aarch64-osx/bec42d753e3ec4d4c6b0f52206494a6c43d3e04804ffd48e2b5058b5e0f67e08/9.0.2/bin"
libdir     = "/Users/azermahjoub/SWTPP/HA/ploy/.stack-work/install/aarch64-osx/bec42d753e3ec4d4c6b0f52206494a6c43d3e04804ffd48e2b5058b5e0f67e08/9.0.2/lib/aarch64-osx-ghc-9.0.2/ploy-1.0.0.0-KZcJqwwmS2ADvCiDOS48lF-validate"
dynlibdir  = "/Users/azermahjoub/SWTPP/HA/ploy/.stack-work/install/aarch64-osx/bec42d753e3ec4d4c6b0f52206494a6c43d3e04804ffd48e2b5058b5e0f67e08/9.0.2/lib/aarch64-osx-ghc-9.0.2"
datadir    = "/Users/azermahjoub/SWTPP/HA/ploy/.stack-work/install/aarch64-osx/bec42d753e3ec4d4c6b0f52206494a6c43d3e04804ffd48e2b5058b5e0f67e08/9.0.2/share/aarch64-osx-ghc-9.0.2/ploy-1.0.0.0"
libexecdir = "/Users/azermahjoub/SWTPP/HA/ploy/.stack-work/install/aarch64-osx/bec42d753e3ec4d4c6b0f52206494a6c43d3e04804ffd48e2b5058b5e0f67e08/9.0.2/libexec/aarch64-osx-ghc-9.0.2/ploy-1.0.0.0"
sysconfdir = "/Users/azermahjoub/SWTPP/HA/ploy/.stack-work/install/aarch64-osx/bec42d753e3ec4d4c6b0f52206494a6c43d3e04804ffd48e2b5058b5e0f67e08/9.0.2/etc"

getBinDir, getLibDir, getDynLibDir, getDataDir, getLibexecDir, getSysconfDir :: IO FilePath
getBinDir = catchIO (getEnv "ploy_bindir") (\_ -> return bindir)
getLibDir = catchIO (getEnv "ploy_libdir") (\_ -> return libdir)
getDynLibDir = catchIO (getEnv "ploy_dynlibdir") (\_ -> return dynlibdir)
getDataDir = catchIO (getEnv "ploy_datadir") (\_ -> return datadir)
getLibexecDir = catchIO (getEnv "ploy_libexecdir") (\_ -> return libexecdir)
getSysconfDir = catchIO (getEnv "ploy_sysconfdir") (\_ -> return sysconfdir)

getDataFileName :: FilePath -> IO FilePath
getDataFileName name = do
  dir <- getDataDir
  return (dir ++ "/" ++ name)
