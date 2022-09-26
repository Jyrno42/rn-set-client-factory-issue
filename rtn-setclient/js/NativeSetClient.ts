import type {TurboModule} from 'react-native';
import {TurboModuleRegistry} from 'react-native';

export interface Spec extends TurboModule {
  getFactory(): string;

  // your module methods go here, for example:
  setValue(id: string): Promise<string>;
  applyConfig(): Promise<string>;
}

export default TurboModuleRegistry.getEnforcing<Spec>('RTNSetClient');
