"""Single source of truth for config. Env vars override config.ini."""
import os
from pathlib import Path

_config = None


def _load_config():
    global _config
    if _config is not None:
        return _config
    try:
        import configparser
    except ImportError:
        import ConfigParser as configparser
    parser = configparser.ConfigParser()
    # Preserve option case (default optionxform lowercases, so baseUrl would become baseurl)
    parser.optionxform = lambda option: option
    root = Path(__file__).resolve().parent.parent
    config_path = root / "config.ini"
    if not config_path.exists():
        raise RuntimeError(f"Cannot find config.ini at {config_path}")
    parser.read(config_path, encoding="utf-8")
    _config = {}
    for section in parser.sections():
        for key, value in parser.items(section):
            _config[key] = value.strip()
    return _config


def get(key: str, default: str = None) -> str:
    """Get config value: env var (KEY in uppercase) overrides, then config.ini, then default."""
    env_key = key.replace(".", "_").upper()
    value = os.environ.get(env_key) or _load_config().get(key, default)
    if value is None or (isinstance(value, str) and not value.strip()):
        return default
    return value.strip()
