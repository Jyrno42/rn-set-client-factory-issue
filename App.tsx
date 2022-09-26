/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 *
 * Generated with the TypeScript template
 * https://github.com/react-native-community/react-native-template-typescript
 *
 * @format
 */

import React, {useState, type PropsWithChildren} from 'react';
import {
  Button,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Text,
  useColorScheme,
  View,
} from 'react-native';

import {Colors, Header} from 'react-native/Libraries/NewAppScreen';

import CustomClientRN from 'rtn-setclient/js/NativeSetClient';

const Section: React.FC<
  PropsWithChildren<{
    title: string;
  }>
> = ({children, title}) => {
  const isDarkMode = useColorScheme() === 'dark';
  return (
    <View style={styles.sectionContainer}>
      <Text
        style={[
          styles.sectionTitle,
          {
            color: isDarkMode ? Colors.white : Colors.black,
          },
        ]}>
        {title}
      </Text>
      <Text
        style={[
          styles.sectionDescription,
          {
            color: isDarkMode ? Colors.light : Colors.dark,
          },
        ]}>
        {children}
      </Text>
    </View>
  );
};

const App = () => {
  const isDarkMode = useColorScheme() === 'dark';

  const backgroundStyle = {
    backgroundColor: isDarkMode ? Colors.darker : Colors.lighter,
  };

  console.log('CustomClientRN', CustomClientRN.getFactory);

  const factory = CustomClientRN.getFactory();

  const title = `Factory: ${factory}`;

  const [response, setResponse] = useState<string | null>(null);

  const doRequestWithClient = async () => {
    if (factory === 'dynamic') {
      await CustomClientRN.setValue('manually set from JS');
      await CustomClientRN.applyConfig();
    }

    const response = await fetch('http://127.0.0.1:8080');
    setResponse(await response.text());
  };

  const withRequestBefore = async () => {
    // Do a random fetch beforehand to make RN initialize the okhttp client
    await fetch('http://example.com');

    await doRequestWithClient();
  };

  return (
    <SafeAreaView style={backgroundStyle}>
      <StatusBar
        barStyle={isDarkMode ? 'light-content' : 'dark-content'}
        backgroundColor={backgroundStyle.backgroundColor}
      />
      <ScrollView
        contentInsetAdjustmentBehavior="automatic"
        style={backgroundStyle}>
        <Header />
        <View
          style={{
            backgroundColor: isDarkMode ? Colors.black : Colors.white,
          }}>
          <Section title={title}>
            Make sure to start the demo server and then hit the button below to
            make a request.
            {'\n\n'}
            {factory === 'static' ? (
              <>
                Observe how the response is the same regardless of the order of
                fetch calls.
              </>
            ) : (
              <>
                Observe how the response differs depending on whether a request
                has or has not been made before setting up the custom client
                factory.
                {'\n\n'}
                Note: Make sure to fully restart the app before attempting a
                different scenario.
              </>
            )}
            {'\n\n'}
            <Button onPress={doRequestWithClient} title="no request" />
            <Button onPress={withRequestBefore} title="with request" />
            {'\n\n'}
            Response:
            {'\n'}
            {response ?? '-'}
          </Section>

          <Section title=""></Section>
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    marginTop: 8,
    fontSize: 18,
    fontWeight: '400',
  },
  highlight: {
    fontWeight: '700',
  },
});

export default App;
